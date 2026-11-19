package com.ruoyi.takeout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.takeout.domain.TakeoutStatusLog;
import com.ruoyi.takeout.mapper.TakeoutStatusLogMapper;
import com.ruoyi.takeout.service.TakeoutStatusLogService;

/**
 * 外卖业务状态日志 业务层实现（内部辅助）
 *
 * @author ruoyi
 */
@Service
public class TakeoutStatusLogServiceImpl implements TakeoutStatusLogService
{
    @Autowired
    private TakeoutStatusLogMapper statusLogMapper;

    @Override
    public void log(String bizType, Long bizId, String fromStatus, String toStatus)
    {
        TakeoutStatusLog log = new TakeoutStatusLog();
        log.setBizType(bizType);
        log.setBizId(bizId);
        log.setFromStatus(fromStatus == null ? "" : fromStatus);
        log.setToStatus(toStatus);
        try
        {
            log.setOperatorId(SecurityUtils.getUserId());
            log.setOperatorName(SecurityUtils.getUsername());
        }
        catch (Exception ignored)
        {
            // 异步/非登录场景忽略
        }
        statusLogMapper.insertStatusLog(log);
    }

    @Override
    public List<TakeoutStatusLog> list(String bizType, Long bizId)
    {
        TakeoutStatusLog query = new TakeoutStatusLog();
        query.setBizType(bizType);
        query.setBizId(bizId);
        return statusLogMapper.selectStatusLogList(query);
    }
}
