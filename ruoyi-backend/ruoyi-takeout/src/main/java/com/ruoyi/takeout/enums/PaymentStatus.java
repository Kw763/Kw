package com.ruoyi.takeout.enums;

/**
 * 外卖模拟支付状态枚举
 * 单向状态机：UNPAID -> SUCCESS
 *
 * @author ruoyi
 */
public enum PaymentStatus
{
    /** 未支付 */
    UNPAID,
    /** 支付成功 */
    SUCCESS;

    /**
     * 判断是否可以迁移到目标状态
     *
     * @param target 目标状态
     * @return 是否允许迁移
     */
    public boolean canTransitionTo(PaymentStatus target)
    {
        return this == UNPAID && target == SUCCESS;
    }
}
