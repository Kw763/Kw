package com.ruoyi.takeout.mapper;

import org.apache.ibatis.annotations.Param;
import com.ruoyi.takeout.domain.TakeoutPayment;

public interface TakeoutPaymentMapper
{
    int insertPayment(TakeoutPayment payment);
    TakeoutPayment selectPaymentByOrderId(Long orderId);
    TakeoutPayment selectPaymentByOrderIdForUpdate(Long orderId);
    int markPaymentSuccess(@Param("paymentId") Long paymentId, @Param("version") Integer version,
            @Param("transactionNo") String transactionNo, @Param("updateBy") String updateBy);
}
