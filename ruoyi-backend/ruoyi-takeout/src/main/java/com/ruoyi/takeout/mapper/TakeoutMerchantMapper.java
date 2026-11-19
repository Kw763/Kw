package com.ruoyi.takeout.mapper;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutMerchant;

public interface TakeoutMerchantMapper
{
    List<TakeoutMerchant> selectMerchantList(TakeoutMerchant query);
    TakeoutMerchant selectMerchantById(Long merchantId);
    TakeoutMerchant selectMerchantByOwnerId(Long ownerUserId);
    int insertMerchant(TakeoutMerchant merchant);
    int updateMerchant(TakeoutMerchant merchant);
    int deleteMerchantById(Long merchantId);
}
