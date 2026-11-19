package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;

/**
 * 外卖骑手端聚合 Service 接口
 *
 * @author ruoyi
 */
public interface ITakeoutRiderService
{
    List<TakeoutDeliveryTask> selectMyTaskList(TakeoutDeliveryTask query);

    TakeoutDeliveryTask selectTaskById(Long taskId);

    int acceptTask(Long taskId);

    int arriveTask(Long taskId);

    int pickupTask(Long taskId);

    int deliverTask(Long taskId);
}
