package com.ruoyi.takeout.enums;

/**
 * 外卖订单状态枚举
 * 严格单向状态机：WAIT_PAY -> WAIT_ACCEPT -> PREPARING -> READY -> DELIVERING -> FINISHED
 *
 * @author ruoyi
 */
public enum OrderStatus
{
    /** 待支付 */
    WAIT_PAY,
    /** 待商家接单 */
    WAIT_ACCEPT,
    /** 商家备餐中 */
    PREPARING,
    /** 待骑手取餐 */
    READY,
    /** 骑手配送中 */
    DELIVERING,
    /** 已完成 */
    FINISHED;

    /**
     * 判断是否可以迁移到目标状态
     *
     * @param target 目标状态
     * @return 是否允许迁移
     */
    public boolean canTransitionTo(OrderStatus target)
    {
        return switch (this)
        {
            case WAIT_PAY -> target == WAIT_ACCEPT;
            case WAIT_ACCEPT -> target == PREPARING;
            case PREPARING -> target == READY;
            case READY -> target == DELIVERING;
            case DELIVERING -> target == FINISHED;
            case FINISHED -> false;
        };
    }
}
