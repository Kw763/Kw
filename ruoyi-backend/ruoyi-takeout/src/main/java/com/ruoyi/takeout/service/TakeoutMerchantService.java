package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutMerchant;

/**
 * 外卖商家 业务层
 *
 * @author ruoyi
 */
public interface TakeoutMerchantService
{
    List<TakeoutMerchant> list(TakeoutMerchant merchant);

    TakeoutMerchant queryById(Long merchantId);

    TakeoutMerchant queryByOwnerUserId(Long ownerUserId);

    int save(TakeoutMerchant merchant);

    int update(TakeoutMerchant merchant);

    int delete(Long merchantId);
}
