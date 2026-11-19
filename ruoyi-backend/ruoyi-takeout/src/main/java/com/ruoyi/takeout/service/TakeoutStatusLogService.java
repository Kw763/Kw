package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutStatusLog;

/**
 * 外卖业务状态日志 业务层（内部辅助）
 *
 * @author ruoyi
 */
public interface TakeoutStatusLogService
{
    /**
     * 写一条状态变更日志
     *
     * @param bizType    业务类型（ORDER / PAYMENT / DELIVERY）
     * @param bizId      业务ID
     * @param fromStatus 原状态
     * @param toStatus   目标状态
     */
    void log(String bizType, Long bizId, String fromStatus, String toStatus);

    /**
     * 查询某业务的所有状态日志
     *
     * @param bizType 业务类型
     * @param bizId   业务ID
     * @return 日志集合
     */
    List<TakeoutStatusLog> list(String bizType, Long bizId);
}
