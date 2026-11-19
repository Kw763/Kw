package com.ruoyi.takeout.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.dto.AssignDeliveryRequest;
import com.ruoyi.takeout.enums.DeliveryStatus;
import com.ruoyi.takeout.enums.OrderStatus;
import com.ruoyi.takeout.mapper.TakeoutDeliveryMapper;
import com.ruoyi.takeout.mapper.TakeoutOrderMapper;
import com.ruoyi.takeout.service.TakeoutDeliveryService;
import com.ruoyi.takeout.service.TakeoutStatusLogService;

/**
 * 外卖配送 业务层实现
 *
 * @author ruoyi
 */
@Service
public class TakeoutDeliveryServiceImpl implements TakeoutDeliveryService
{
    @Autowired
    private TakeoutDeliveryMapper deliveryMapper;

    @Autowired
    private TakeoutOrderMapper orderMapper;

    @Autowired
    private TakeoutStatusLogService statusLogService;

    /**
     * 条件更新配送任务状态，带 rider_id 归属和乐观锁。
     * 失败时读取当前状态，若已为目标状态返回成功，否则抛 ServiceException。
     */
    private int transitionStatus(Long taskId, Long riderId, String fromStatus, String toStatus,
            TakeoutDeliveryTask latestRef)
    {
        String updateBy = SecurityUtils.getUsername();
        TakeoutDeliveryTask latest = latestRef;
        if (latest == null)
        {
            latest = deliveryMapper.selectDeliveryById(taskId);
        }
        if (latest == null)
        {
            throw new ServiceException("配送任务不存在");
        }
        // 归属校验
        if (riderId != null && !riderId.equals(latest.getRiderId()))
        {
            throw new ServiceException("该配送任务不属于当前骑手");
        }

        DeliveryStatus from = DeliveryStatus.valueOf(fromStatus);
        DeliveryStatus to = DeliveryStatus.valueOf(toStatus);
        if (!from.canTransitionTo(to))
        {
            throw new ServiceException("配送状态不允许从 " + fromStatus + " 变更为 " + toStatus);
        }

        int rows = deliveryMapper.updateDeliveryStatus(taskId, riderId, fromStatus, toStatus, latest.getVersion(),
                updateBy);
        if (rows == 0)
        {
            // 条件更新失败，重新查询当前状态
            TakeoutDeliveryTask current = deliveryMapper.selectDeliveryById(taskId);
            if (current != null && toStatus.equals(current.getStatus()))
            {
                // 已是目标状态，视为重复请求成功
                return 1;
            }
            if (current == null)
            {
                throw new ServiceException("配送任务不存在");
            }
            throw new ServiceException("配送任务当前状态为 " + current.getStatus() + "，无法变更");
        }
        // 写日志
        statusLogService.log("DELIVERY", taskId, fromStatus, toStatus);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assign(Long taskId, AssignDeliveryRequest request)
    {
        TakeoutDeliveryTask task = deliveryMapper.selectDeliveryById(taskId);
        if (task == null)
        {
            throw new ServiceException("配送任务不存在");
        }
        DeliveryStatus fromStatus = DeliveryStatus.valueOf(task.getStatus());
        if (!DeliveryStatus.UNASSIGNED.equals(fromStatus))
        {
            throw new ServiceException("当前任务状态为 " + task.getStatus() + "，不允许派单");
        }
        // rider_id 归属（派单时 rider 为空，传 null 跳过校验）
        String updateBy = SecurityUtils.getUsername();
        int rows = deliveryMapper.assignDelivery(taskId, request.riderId(), task.getVersion(), updateBy);
        if (rows == 0)
        {
            TakeoutDeliveryTask current = deliveryMapper.selectDeliveryById(taskId);
            if (current != null && DeliveryStatus.ASSIGNED.name().equals(current.getStatus()))
            {
                return 1;
            }
            throw new ServiceException("派单失败，任务状态可能已变更");
        }
        statusLogService.log("DELIVERY", taskId, DeliveryStatus.UNASSIGNED.name(), DeliveryStatus.ASSIGNED.name());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int accept(Long taskId)
    {
        TakeoutDeliveryTask task = deliveryMapper.selectDeliveryById(taskId);
        if (task == null)
        {
            throw new ServiceException("配送任务不存在");
        }
        Long riderId = SecurityUtils.getUserId();
        if (task.getRiderId() == null || !riderId.equals(task.getRiderId()))
        {
            throw new ServiceException("该配送任务未指派给当前骑手");
        }
        int rows = transitionStatus(taskId, riderId, task.getStatus(), DeliveryStatus.ACCEPTED.name(), task);
        // 同时更新 acceptTime
        deliveryMapper.selectDeliveryById(taskId); // 确保后续 selectOrderItems 前的隔离...
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int arrive(Long taskId)
    {
        TakeoutDeliveryTask task = deliveryMapper.selectDeliveryById(taskId);
        if (task == null)
        {
            throw new ServiceException("配送任务不存在");
        }
        Long riderId = SecurityUtils.getUserId();
        return transitionStatus(taskId, riderId, task.getStatus(), DeliveryStatus.ARRIVED.name(), task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pickup(Long taskId)
    {
        TakeoutDeliveryTask task = deliveryMapper.selectDeliveryById(taskId);
        if (task == null)
        {
            throw new ServiceException("配送任务不存在");
        }
        Long riderId = SecurityUtils.getUserId();
        // 1) 骑手取餐：配送任务 ARRIVED → PICKED_UP
        int rows = transitionStatus(taskId, riderId, task.getStatus(), DeliveryStatus.PICKED_UP.name(), task);

        // 2) 同步订单：READY → DELIVERING
        Long orderId = task.getOrderId();
        TakeoutOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("关联订单不存在");
        }
        OrderStatus oFrom = OrderStatus.valueOf(order.getStatus());
        if (!oFrom.canTransitionTo(OrderStatus.DELIVERING))
        {
            throw new ServiceException("订单状态 " + order.getStatus() + " 不允许流转到 DELIVERING");
        }
        int oRows = orderMapper.updateOrderStatus(orderId, order.getStatus(), OrderStatus.DELIVERING.name(),
                order.getVersion(), SecurityUtils.getUsername());
        if (oRows == 0)
        {
            // 订单已被更新，读最新状态
            TakeoutOrder current = orderMapper.selectOrderById(orderId);
            if (current != null && OrderStatus.DELIVERING.name().equals(current.getStatus()))
            {
                // 重复请求
            }
            else
            {
                throw new ServiceException("订单状态更新失败，当前状态 " + (current == null ? "未知" : current.getStatus()));
            }
        }
        else
        {
            statusLogService.log("ORDER", orderId, order.getStatus(), OrderStatus.DELIVERING.name());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deliver(Long taskId)
    {
        TakeoutDeliveryTask task = deliveryMapper.selectDeliveryById(taskId);
        if (task == null)
        {
            throw new ServiceException("配送任务不存在");
        }
        Long riderId = SecurityUtils.getUserId();
        // 1) 骑手送达：配送任务 PICKED_UP → DELIVERED
        int rows = transitionStatus(taskId, riderId, task.getStatus(), DeliveryStatus.DELIVERED.name(), task);

        // 2) 同步订单：DELIVERING → FINISHED
        Long orderId = task.getOrderId();
        TakeoutOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            throw new ServiceException("关联订单不存在");
        }
        OrderStatus oFrom = OrderStatus.valueOf(order.getStatus());
        if (!oFrom.canTransitionTo(OrderStatus.FINISHED))
        {
            throw new ServiceException("订单状态 " + order.getStatus() + " 不允许流转到 FINISHED");
        }
        int oRows = orderMapper.updateOrderStatus(orderId, order.getStatus(), OrderStatus.FINISHED.name(),
                order.getVersion(), SecurityUtils.getUsername());
        if (oRows == 0)
        {
            TakeoutOrder current = orderMapper.selectOrderById(orderId);
            if (!(current != null && OrderStatus.FINISHED.name().equals(current.getStatus())))
            {
                throw new ServiceException("订单状态更新失败，当前状态 " + (current == null ? "未知" : current.getStatus()));
            }
        }
        else
        {
            statusLogService.log("ORDER", orderId, order.getStatus(), OrderStatus.FINISHED.name());
        }
        return rows;
    }

    @Override
    public List<TakeoutDeliveryTask> listForRider(TakeoutDeliveryTask task)
    {
        task.setRiderId(SecurityUtils.getUserId());
        return deliveryMapper.selectDeliveryList(task);
    }

    @Override
    public TakeoutDeliveryTask detail(Long taskId)
    {
        return deliveryMapper.selectDeliveryById(taskId);
    }
}
