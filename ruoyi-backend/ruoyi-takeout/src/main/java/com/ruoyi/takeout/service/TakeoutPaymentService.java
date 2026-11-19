package com.ruoyi.takeout.service;

import com.ruoyi.takeout.domain.TakeoutPayment;

/**
 * 外卖模拟支付 业务层（内部辅助，不对外暴露 Controller）
 *
 * @author ruoyi
 */
public interface TakeoutPaymentService
{
    /**
     * 根据订单ID查询支付单
     *
     * @param orderId 订单ID
     * @return 支付单
     */
    TakeoutPayment queryByOrderId(Long orderId);

    /**
     * 标记支付成功（PaymentStatus.UNPAID → SUCCESS）。
     * 条件更新失败时若已是 SUCCESS 视为重复请求返回成功，否则抛 ServiceException。
     *
     * @param payment       原支付单（带 paymentId 和 version）
     * @param transactionNo 模拟交易流水号
     * @return 影响行数
     */
    int markSuccess(TakeoutPayment payment, String transactionNo);
}
