package com.ruoyi.takeout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.service.ITakeoutRiderService;
import com.ruoyi.takeout.service.TakeoutDeliveryService;

/**
 * 外卖骑手端聚合 Service 实现
 *
 * @author ruoyi
 */
@Service
public class ITakeoutRiderServiceImpl implements ITakeoutRiderService
{
    @Autowired
    private TakeoutDeliveryService deliveryService;

    @Override
    public List<TakeoutDeliveryTask> selectMyTaskList(TakeoutDeliveryTask query)
    {
        return deliveryService.listForRider(query);
    }

    @Override
    public TakeoutDeliveryTask selectTaskById(Long taskId)
    {
        return deliveryService.detail(taskId);
    }

    @Override
    public int acceptTask(Long taskId)
    {
        return deliveryService.accept(taskId);
    }

    @Override
    public int arriveTask(Long taskId)
    {
        return deliveryService.arrive(taskId);
    }

    @Override
    public int pickupTask(Long taskId)
    {
        return deliveryService.pickup(taskId);
    }

    @Override
    public int deliverTask(Long taskId)
    {
        return deliveryService.deliver(taskId);
    }
}
