package com.ruoyi.takeout.service.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.takeout.domain.TakeoutPayment;
import com.ruoyi.takeout.enums.PaymentStatus;
import com.ruoyi.takeout.mapper.TakeoutPaymentMapper;
import com.ruoyi.takeout.service.TakeoutPaymentService;
import com.ruoyi.takeout.service.TakeoutStatusLogService;

/**
 * 外卖模拟支付 业务层实现（内部辅助）
 *
 * @author ruoyi
 */
@Service
public class TakeoutPaymentServiceImpl implements TakeoutPaymentService
{
    @Autowired
    private TakeoutPaymentMapper paymentMapper;

    @Autowired
    private TakeoutStatusLogService statusLogService;

    @Override
    public TakeoutPayment queryByOrderId(Long orderId)
    {
        return paymentMapper.selectPaymentByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markSuccess(TakeoutPayment payment, String transactionNo)
    {
        if (payment == null)
        {
            throw new ServiceException("支付单不存在");
        }
        String fromStatus = payment.getStatus();
        String toStatus = PaymentStatus.SUCCESS.name();
        if (toStatus.equals(fromStatus))
        {
            return 1; // 重复请求
        }
        if (!PaymentStatus.UNPAID.name().equals(fromStatus))
        {
            throw new ServiceException("支付状态异常，当前为 " + fromStatus);
        }
        String updateBy = SecurityUtils.getUsername();
        int rows = paymentMapper.markPaymentSuccess(payment.getPaymentId(), payment.getVersion(), transactionNo, updateBy);
        if (rows == 0)
        {
            TakeoutPayment latest = paymentMapper.selectPaymentByOrderId(payment.getOrderId());
            if (latest != null && PaymentStatus.SUCCESS.name().equals(latest.getStatus()))
            {
                return 1;
            }
            throw new ServiceException("支付单状态已变更，请刷新后重试");
        }
        statusLogService.log("PAYMENT", payment.getPaymentId(), fromStatus, toStatus);
        return rows;
    }
}
