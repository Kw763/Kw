package com.ruoyi.takeout.mapper;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutStatusLog;

public interface TakeoutStatusLogMapper
{
    int insertStatusLog(TakeoutStatusLog log);
    List<TakeoutStatusLog> selectStatusLogList(TakeoutStatusLog query);
    TakeoutStatusLog selectStatusLogById(Long logId);
}
