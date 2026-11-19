package com.ruoyi.takeout.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.domain.TakeoutMerchant;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutOrderItem;
import com.ruoyi.takeout.domain.TakeoutPayment;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.dto.CreateOrderRequest;
import com.ruoyi.takeout.enums.DeliveryStatus;
import com.ruoyi.takeout.enums.OrderStatus;
import com.ruoyi.takeout.enums.PaymentStatus;
import com.ruoyi.takeout.mapper.TakeoutAddressMapper;
import com.ruoyi.takeout.mapper.TakeoutDeliveryMapper;
import com.ruoyi.takeout.mapper.TakeoutMerchantMapper;
import com.ruoyi.takeout.mapper.TakeoutOrderMapper;
import com.ruoyi.takeout.mapper.TakeoutPaymentMapper;
import com.ruoyi.takeout.mapper.TakeoutProductMapper;
import com.ruoyi.takeout.service.TakeoutOrderService;
import com.ruoyi.takeout.service.TakeoutStatusLogService;

/**
 * 外卖订单 业务层实现
 *
 * @author ruoyi
 */
@Service
public class TakeoutOrderServiceImpl implements TakeoutOrderService
{
    @Autowired
    private TakeoutOrderMapper orderMapper;

    @Autowired
    private TakeoutProductMapper productMapper;

    @Autowired
    private TakeoutMerchantMapper merchantMapper;

    @Autowired
    private TakeoutAddressMapper addressMapper;

    @Autowired
    private TakeoutPaymentMapper paymentMapper;

    @Autowired
    private TakeoutDeliveryMapper deliveryMapper;

    @Autowired
    private TakeoutStatusLogService statusLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TakeoutOrder create(CreateOrderRequest request)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        // 0) 幂等：先查 (userId, requestNo)
        TakeoutOrder existing = orderMapper.selectOrderByUserRequest(userId, request.requestNo());
        if (existing != null)
        {
            return detail(existing.getOrderId());
        }

        // 1) 校验地址属于当前用户
        TakeoutAddress address = addressMapper.selectAddressByIdAndUserId(request.addressId(), userId);
        if (address == null)
        {
            throw new ServiceException("收货地址不存在或不属于当前用户");
        }

        // 2) 校验商家存在且营业中（status=0）
        TakeoutMerchant merchant = merchantMapper.selectMerchantById(request.merchantId());
        if (merchant == null)
        {
            throw new ServiceException("商家不存在");
        }
        if (!"0".equals(merchant.getStatus()))
        {
            throw new ServiceException("商家当前休息中，无法下单");
        }

        // 3) 校验所有商品同属该商家且处于上架状态；同时服务端计价
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<TakeoutOrderItem> items = new ArrayList<>();
        for (CreateOrderRequest.CreateOrderItem itemReq : request.items())
        {
            TakeoutProduct product = productMapper.selectActiveProductById(itemReq.productId());
            if (product == null)
            {
                throw new ServiceException("商品不存在或已下架（productId=" + itemReq.productId() + "）");
            }
            if (!product.getMerchantId().equals(merchant.getMerchantId()))
            {
                throw new ServiceException("商品不属于指定商家（productId=" + itemReq.productId() + "）");
            }
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity()));
            totalAmount = totalAmount.add(subtotal);

            TakeoutOrderItem item = new TakeoutOrderItem();
            item.setProductId(product.getProductId());
            item.setProductName(product.getProductName());
            item.setProductImage(product.getImage());
            item.setProductPrice(product.getPrice());
            item.setQuantity(itemReq.quantity());
            item.setSubtotal(subtotal);
            items.add(item);
        }

        // 4) 组装并写订单
        TakeoutOrder order = new TakeoutOrder();
        order.setOrderNo(IdUtils.fastSimpleUUID());
        order.setRequestNo(request.requestNo());
        order.setUserId(userId);
        order.setMerchantId(merchant.getMerchantId());
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        String fullAddress = address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress();
        order.setDeliveryAddress(fullAddress);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.WAIT_PAY.name());
        order.setCreateBy(username);
        order.setRemark(request.remark());

        orderMapper.insertOrder(order);

        // 5) 写订单明细快照
        for (TakeoutOrderItem item : items)
        {
            item.setOrderId(order.getOrderId());
            orderMapper.insertOrderItem(item);
        }

        // 6) 写支付单
        TakeoutPayment payment = new TakeoutPayment();
        payment.setPaymentNo("P" + order.getOrderNo());
        payment.setOrderId(order.getOrderId());
        payment.setUserId(userId);
        payment.setAmount(totalAmount);
        payment.setStatus(PaymentStatus.UNPAID.name());
        payment.setCreateBy(username);
        paymentMapper.insertPayment(payment);

        // 7) 写状态日志（创建）
        statusLogService.log("ORDER", order.getOrderId(), "", OrderStatus.WAIT_PAY.name());
        statusLogService.log("PAYMENT", payment.getPaymentId(), "", PaymentStatus.UNPAID.name());

        return detail(order.getOrderId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int mockPay(Long orderId)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        if (!userId.equals(order.getUserId()))
        {
            throw new ServiceException("无权操作他人订单");
        }

        // 1) 订单状态：WAIT_PAY → WAIT_ACCEPT
        String orderFrom = order.getStatus();
        if (OrderStatus.WAIT_ACCEPT.name().equals(orderFrom))
        {
            // 重复请求，直接返回
            return 1;
        }
        OrderStatus oFrom = OrderStatus.valueOf(orderFrom);
        if (!OrderStatus.WAIT_PAY.equals(oFrom))
        {
            throw new ServiceException("订单当前状态 " + orderFrom + " 不允许支付");
        }

        int orderRows = orderMapper.updateOrderStatus(orderId, orderFrom, OrderStatus.WAIT_ACCEPT.name(),
                order.getVersion(), username);
        if (orderRows == 0)
        {
            TakeoutOrder current = orderMapper.selectOrderById(orderId);
            if (current != null && OrderStatus.WAIT_ACCEPT.name().equals(current.getStatus()))
            {
                return 1;
            }
            throw new ServiceException("订单状态已变更，请刷新后重试");
        }
        statusLogService.log("ORDER", orderId, orderFrom, OrderStatus.WAIT_ACCEPT.name());

        // 2) 支付单：UNPAID → SUCCESS
        TakeoutPayment payment = paymentMapper.selectPaymentByOrderId(orderId);
        if (payment == null)
        {
            throw new ServiceException("支付单不存在");
        }
        String payFrom = payment.getStatus();
        if (PaymentStatus.SUCCESS.name().equals(payFrom))
        {
            return 1;
        }
        int payRows = paymentMapper.markPaymentSuccess(payment.getPaymentId(), payment.getVersion(),
                UUID.randomUUID().toString(), username);
        if (payRows == 0)
        {
            TakeoutPayment latest = paymentMapper.selectPaymentByOrderId(orderId);
            if (!(latest != null && PaymentStatus.SUCCESS.name().equals(latest.getStatus())))
            {
                throw new ServiceException("支付单状态已变更");
            }
        }
        else
        {
            statusLogService.log("PAYMENT", payment.getPaymentId(), payFrom, PaymentStatus.SUCCESS.name());
        }

        return orderRows;
    }

    @Override
    public List<TakeoutOrder> listForUser(TakeoutOrder order)
    {
        order.setUserId(SecurityUtils.getUserId());
        return orderMapper.selectOrderList(order);
    }

    @Override
    public List<TakeoutOrder> listForMerchant(TakeoutOrder order)
    {
        // 商家查询本店订单
        TakeoutMerchant merchant = merchantMapper.selectMerchantByOwnerId(SecurityUtils.getUserId());
        if (merchant == null)
        {
            throw new ServiceException("当前用户未绑定商家");
        }
        order.setMerchantId(merchant.getMerchantId());
        return orderMapper.selectOrderList(order);
    }

    @Override
    public List<TakeoutOrder> listForAdmin(TakeoutOrder order)
    {
        if (!SecurityUtils.isAdmin())
        {
            throw new ServiceException("仅管理员可查询全部订单");
        }
        return orderMapper.selectOrderList(order);
    }

    @Override
    public TakeoutOrder detail(Long orderId)
    {
        TakeoutOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            return null;
        }
        order.setItems(orderMapper.selectOrderItems(orderId));
        order.setPayment(paymentMapper.selectPaymentByOrderId(orderId));
        TakeoutDeliveryTask delivery = deliveryMapper.selectDeliveryByOrderId(orderId);
        order.setDeliveryTask(delivery);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int accept(Long orderId)
    {
        TakeoutOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        // 校验归属：当前登录用户必须是商家 owner
        TakeoutMerchant merchant = merchantMapper.selectMerchantByOwnerId(SecurityUtils.getUserId());
        if (merchant == null || !merchant.getMerchantId().equals(order.getMerchantId()))
        {
            throw new ServiceException("无权操作该订单");
        }

        String fromStatus = order.getStatus();
        if (OrderStatus.PREPARING.name().equals(fromStatus))
        {
            return 1; // 重复
        }
        OrderStatus from = OrderStatus.valueOf(fromStatus);
        if (!OrderStatus.WAIT_ACCEPT.equals(from))
        {
            throw new ServiceException("订单状态 " + fromStatus + " 不允许接单");
        }

        int rows = orderMapper.updateOrderStatus(orderId, fromStatus, OrderStatus.PREPARING.name(),
                order.getVersion(), SecurityUtils.getUsername());
        if (rows == 0)
        {
            TakeoutOrder current = orderMapper.selectOrderById(orderId);
            if (current != null && OrderStatus.PREPARING.name().equals(current.getStatus()))
            {
                return 1;
            }
            throw new ServiceException("订单状态已变更，请刷新后重试");
        }
        statusLogService.log("ORDER", orderId, fromStatus, OrderStatus.PREPARING.name());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markReady(Long orderId)
    {
        TakeoutOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        // 校验归属
        TakeoutMerchant merchant = merchantMapper.selectMerchantByOwnerId(SecurityUtils.getUserId());
        if (merchant == null || !merchant.getMerchantId().equals(order.getMerchantId()))
        {
            throw new ServiceException("无权操作该订单");
        }

        String fromStatus = order.getStatus();
        if (OrderStatus.READY.name().equals(fromStatus))
        {
            // 已出餐，检查是否已有配送任务
            ensureDeliveryTaskExists(order, merchant);
            return 1;
        }
        OrderStatus from = OrderStatus.valueOf(fromStatus);
        if (!OrderStatus.PREPARING.equals(from))
        {
            throw new ServiceException("订单状态 " + fromStatus + " 不允许出餐");
        }

        int rows = orderMapper.updateOrderStatus(orderId, fromStatus, OrderStatus.READY.name(),
                order.getVersion(), SecurityUtils.getUsername());
        if (rows == 0)
        {
            TakeoutOrder current = orderMapper.selectOrderById(orderId);
            if (current != null && OrderStatus.READY.name().equals(current.getStatus()))
            {
                ensureDeliveryTaskExists(order, merchant);
                return 1;
            }
            throw new ServiceException("订单状态已变更，请刷新后重试");
        }
        statusLogService.log("ORDER", orderId, fromStatus, OrderStatus.READY.name());

        // 创建配送任务
        ensureDeliveryTaskExists(order, merchant);

        return rows;
    }

    /**
     * 为订单创建配送任务（如果尚不存在）。
     */
    private void ensureDeliveryTaskExists(TakeoutOrder order, TakeoutMerchant merchant)
    {
        TakeoutDeliveryTask existing = deliveryMapper.selectDeliveryByOrderId(order.getOrderId());
        if (existing != null)
        {
            return;
        }
        TakeoutDeliveryTask task = new TakeoutDeliveryTask();
        task.setTaskNo("D" + order.getOrderNo());
        task.setOrderId(order.getOrderId());
        task.setStatus(DeliveryStatus.UNASSIGNED.name());
        task.setMerchantName(merchant.getMerchantName());
        task.setMerchantAddress(merchant.getAddress());
        task.setReceiverName(order.getReceiverName());
        task.setReceiverPhone(order.getReceiverPhone());
        task.setDeliveryAddress(order.getDeliveryAddress());
        task.setCreateBy(SecurityUtils.getUsername());
        deliveryMapper.insertDelivery(task);
        statusLogService.log("DELIVERY", task.getTaskId(), "", DeliveryStatus.UNASSIGNED.name());
    }
}
