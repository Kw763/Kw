package com.ruoyi.takeout.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutOrderItem;

public interface TakeoutOrderMapper
{
    List<TakeoutOrder> selectOrderList(TakeoutOrder query);
    TakeoutOrder selectOrderById(Long orderId);
    TakeoutOrder selectOrderByIdForUpdate(Long orderId);
    TakeoutOrder selectOrderByUserRequest(@Param("userId") Long userId, @Param("requestNo") String requestNo);
    int insertOrder(TakeoutOrder order);
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("fromStatus") String fromStatus,
            @Param("toStatus") String toStatus, @Param("version") Integer version,
            @Param("updateBy") String updateBy);
    int insertOrderItem(TakeoutOrderItem item);
    List<TakeoutOrderItem> selectOrderItems(Long orderId);
}
