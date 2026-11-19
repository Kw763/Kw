package com.ruoyi.takeout.enums;

/**
 * 外卖配送状态枚举
 * 严格单向状态机：UNASSIGNED -> ASSIGNED -> ACCEPTED -> ARRIVED -> PICKED_UP -> DELIVERED
 *
 * @author ruoyi
 */
public enum DeliveryStatus
{
    /** 待派单 */
    UNASSIGNED,
    /** 已派单，待骑手接单 */
    ASSIGNED,
    /** 骑手已接单 */
    ACCEPTED,
    /** 骑手已到店 */
    ARRIVED,
    /** 骑手已取餐 */
    PICKED_UP,
    /** 已送达 */
    DELIVERED;

    /**
     * 判断是否可以迁移到目标状态
     *
     * @param target 目标状态
     * @return 是否允许迁移
     */
    public boolean canTransitionTo(DeliveryStatus target)
    {
        return switch (this)
        {
            case UNASSIGNED -> target == ASSIGNED;
            case ASSIGNED -> target == ACCEPTED;
            case ACCEPTED -> target == ARRIVED;
            case ARRIVED -> target == PICKED_UP;
            case PICKED_UP -> target == DELIVERED;
            case DELIVERED -> false;
        };
    }
}
