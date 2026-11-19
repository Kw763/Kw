package com.ruoyi.takeout.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;

public interface TakeoutDeliveryMapper
{
    List<TakeoutDeliveryTask> selectDeliveryList(TakeoutDeliveryTask query);
    TakeoutDeliveryTask selectDeliveryById(Long taskId);
    TakeoutDeliveryTask selectDeliveryByOrderId(Long orderId);
    TakeoutDeliveryTask selectDeliveryByIdForUpdate(Long taskId);
    TakeoutDeliveryTask selectDeliveryByOrderIdForUpdate(Long orderId);
    int insertDelivery(TakeoutDeliveryTask task);
    int assignDelivery(@Param("taskId") Long taskId, @Param("riderId") Long riderId,
            @Param("version") Integer version, @Param("updateBy") String updateBy);
    int updateDeliveryStatus(@Param("taskId") Long taskId, @Param("riderId") Long riderId,
            @Param("fromStatus") String fromStatus, @Param("toStatus") String toStatus,
            @Param("version") Integer version, @Param("updateBy") String updateBy);
    List<SysUser> selectRiderOptions();
}
