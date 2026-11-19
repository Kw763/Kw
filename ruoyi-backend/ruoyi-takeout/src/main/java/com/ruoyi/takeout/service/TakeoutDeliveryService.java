package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.domain.dto.AssignDeliveryRequest;

/**
 * 外卖配送 业务层
 *
 * @author ruoyi
 */
public interface TakeoutDeliveryService
{
    /**
     * 管理员派单：UNASSIGNED → ASSIGNED
     *
     * @param taskId  配送任务ID
     * @param request 派单请求
     * @return 结果
     */
    int assign(Long taskId, AssignDeliveryRequest request);

    /**
     * 骑手接单：ASSIGNED → ACCEPTED（带 rider_id 归属）
     *
     * @param taskId 配送任务ID
     * @return 结果
     */
    int accept(Long taskId);

    /**
     * 骑手到店：ACCEPTED → ARRIVED
     *
     * @param taskId 配送任务ID
     * @return 结果
     */
    int arrive(Long taskId);

    /**
     * 骑手取餐：ARRIVED → PICKED_UP，同步订单 → DELIVERING
     *
     * @param taskId 配送任务ID
     * @return 结果
     */
    int pickup(Long taskId);

    /**
     * 骑手送达：PICKED_UP → DELIVERED，同步订单 → FINISHED
     *
     * @param taskId 配送任务ID
     * @return 结果
     */
    int deliver(Long taskId);

    /**
     * 骑手查询自己的配送任务列表
     *
     * @param task 查询条件
     * @return 配送任务集合
     */
    List<TakeoutDeliveryTask> listForRider(TakeoutDeliveryTask task);

    /**
     * 查询配送任务详情
     *
     * @param taskId 配送任务ID
     * @return 配送任务
     */
    TakeoutDeliveryTask detail(Long taskId);
}
