package com.ruoyi.takeout.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.ruoyi.takeout.domain.TakeoutStatusLog;
import com.ruoyi.takeout.domain.dto.AddressSaveRequest;
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
import com.ruoyi.takeout.mapper.TakeoutStatusLogMapper;
import com.ruoyi.takeout.service.ITakeoutUserService;

/**
 * 外卖用户端聚合 Service 实现
 *
 * @author ruoyi
 */
@Service
public class ITakeoutUserServiceImpl implements ITakeoutUserService
{
    @Autowired
    private TakeoutMerchantMapper merchantMapper;

    @Autowired
    private TakeoutProductMapper productMapper;

    @Autowired
    private TakeoutAddressMapper addressMapper;

    @Autowired
    private TakeoutOrderMapper orderMapper;

    @Autowired
    private TakeoutPaymentMapper paymentMapper;

    @Autowired
    private TakeoutDeliveryMapper deliveryMapper;

    @Autowired
    private TakeoutStatusLogMapper statusLogMapper;

    // ==================== 地址管理 ====================

    @Override
    public List<TakeoutAddress> selectMyAddressList()
    {
        Long userId = SecurityUtils.getUserId();
        TakeoutAddress query = new TakeoutAddress();
        query.setUserId(userId);
        return addressMapper.selectAddressList(query);
    }

    @Override
    public TakeoutAddress selectAddressById(Long addressId)
    {
        Long userId = SecurityUtils.getUserId();
        TakeoutAddress address = addressMapper.selectAddressByIdAndUserId(addressId, userId);
        if (address == null)
        {
            throw new ServiceException("收货地址不存在或不属于当前用户");
        }
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAddress(AddressSaveRequest request)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutAddress address = new TakeoutAddress();
        address.setUserId(userId);
        address.setReceiverName(request.receiverName());
        address.setReceiverPhone(request.receiverPhone());
        address.setProvince(request.province());
        address.setCity(request.city());
        address.setDistrict(request.district());
        address.setDetailAddress(request.detailAddress());
        address.setIsDefault(request.isDefault() != null ? request.isDefault() : "N");
        address.setCreateBy(username);
        address.setRemark(request.remark());

        // 如果设为默认，先清除其他默认
        if ("Y".equals(address.getIsDefault()))
        {
            addressMapper.clearDefaultAddress(userId, username);
        }

        return addressMapper.insertAddress(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAddress(Long addressId, AddressSaveRequest request)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutAddress existing = addressMapper.selectAddressByIdAndUserId(addressId, userId);
        if (existing == null)
        {
            throw new ServiceException("收货地址不存在或不属于当前用户");
        }

        TakeoutAddress address = new TakeoutAddress();
        address.setAddressId(addressId);
        address.setUserId(userId);
        address.setReceiverName(request.receiverName());
        address.setReceiverPhone(request.receiverPhone());
        address.setProvince(request.province());
        address.setCity(request.city());
        address.setDistrict(request.district());
        address.setDetailAddress(request.detailAddress());
        address.setIsDefault(request.isDefault() != null ? request.isDefault() : existing.getIsDefault());
        address.setUpdateBy(username);
        address.setRemark(request.remark());

        if ("Y".equals(address.getIsDefault()) && !"Y".equals(existing.getIsDefault()))
        {
            addressMapper.clearDefaultAddress(userId, username);
        }

        return addressMapper.updateAddress(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAddress(Long addressId)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        return addressMapper.deleteAddressByIdAndUserId(addressId, userId, username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setDefaultAddress(Long addressId)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutAddress existing = addressMapper.selectAddressByIdAndUserId(addressId, userId);
        if (existing == null)
        {
            throw new ServiceException("收货地址不存在或不属于当前用户");
        }

        addressMapper.clearDefaultAddress(userId, username);
        return addressMapper.setDefaultAddress(addressId, userId, username);
    }

    // ==================== 订单管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TakeoutOrder createOrder(CreateOrderRequest request)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        // 幂等：先查 (userId, requestNo)
        TakeoutOrder existing = orderMapper.selectOrderByUserRequest(userId, request.requestNo());
        if (existing != null)
        {
            return selectOrderDetail(existing.getOrderId());
        }

        // 校验地址属于当前用户
        TakeoutAddress address = addressMapper.selectAddressByIdAndUserId(request.addressId(), userId);
        if (address == null)
        {
            throw new ServiceException("收货地址不存在或不属于当前用户");
        }

        // 校验商家存在且营业中
        TakeoutMerchant merchant = merchantMapper.selectMerchantById(request.merchantId());
        if (merchant == null)
        {
            throw new ServiceException("商家不存在");
        }
        if (!"0".equals(merchant.getStatus()))
        {
            throw new ServiceException("商家当前休息中，无法下单");
        }

        // 校验所有商品同属该商家且上架；同时服务端计价
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

        // 组装并写订单
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

        // 写订单明细快照
        for (TakeoutOrderItem item : items)
        {
            item.setOrderId(order.getOrderId());
            orderMapper.insertOrderItem(item);
        }

        // 写支付单
        TakeoutPayment payment = new TakeoutPayment();
        payment.setPaymentNo("P" + order.getOrderNo());
        payment.setOrderId(order.getOrderId());
        payment.setUserId(userId);
        payment.setAmount(totalAmount);
        payment.setStatus(PaymentStatus.UNPAID.name());
        payment.setCreateBy(username);
        paymentMapper.insertPayment(payment);

        // 写状态日志
        logStatus("ORDER", order.getOrderId(), "", OrderStatus.WAIT_PAY.name());
        logStatus("PAYMENT", payment.getPaymentId(), "", PaymentStatus.UNPAID.name());

        return selectOrderDetail(order.getOrderId());
    }

    @Override
    public List<TakeoutOrder> selectMyOrderList(TakeoutOrder query)
    {
        query.setUserId(SecurityUtils.getUserId());
        return orderMapper.selectOrderList(query);
    }

    @Override
    public TakeoutOrder selectOrderDetail(Long orderId)
    {
        Long userId = SecurityUtils.getUserId();
        TakeoutOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("订单不存在");
        }
        if (!userId.equals(order.getUserId()))
        {
            throw new ServiceException("无权查看他人订单");
        }
        order.setItems(orderMapper.selectOrderItems(orderId));
        order.setPayment(paymentMapper.selectPaymentByOrderId(orderId));
        TakeoutDeliveryTask delivery = deliveryMapper.selectDeliveryByOrderId(orderId);
        order.setDeliveryTask(delivery);
        return order;
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
            return 1; // 重复请求
        }
        OrderStatus oFrom;
        try
        {
            oFrom = OrderStatus.valueOf(orderFrom);
        }
        catch (IllegalArgumentException e)
        {
            throw new ServiceException("订单当前状态 " + orderFrom + " 异常");
        }
        if (!oFrom.canTransitionTo(OrderStatus.WAIT_ACCEPT))
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
        logStatus("ORDER", orderId, orderFrom, OrderStatus.WAIT_ACCEPT.name());

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
            logStatus("PAYMENT", payment.getPaymentId(), payFrom, PaymentStatus.SUCCESS.name());
        }

        return orderRows;
    }

    // ==================== 内部辅助 ====================

    private void logStatus(String bizType, Long bizId, String fromStatus, String toStatus)
    {
        TakeoutStatusLog slog = new TakeoutStatusLog();
        slog.setBizType(bizType);
        slog.setBizId(bizId);
        slog.setFromStatus(fromStatus);
        slog.setToStatus(toStatus);
        slog.setOperatorId(SecurityUtils.getUserId());
        slog.setOperatorName(SecurityUtils.getUsername());
        slog.setCreateBy(SecurityUtils.getUsername());
        statusLogMapper.insertStatusLog(slog);
    }
}
