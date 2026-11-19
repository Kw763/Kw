package com.ruoyi.takeout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.takeout.domain.TakeoutMerchant;
import com.ruoyi.takeout.mapper.TakeoutMerchantMapper;
import com.ruoyi.takeout.service.TakeoutMerchantService;

/**
 * 外卖商家 业务层实现
 *
 * @author ruoyi
 */
@Service
public class TakeoutMerchantServiceImpl implements TakeoutMerchantService
{
    @Autowired
    private TakeoutMerchantMapper merchantMapper;

    @Override
    public List<TakeoutMerchant> list(TakeoutMerchant merchant)
    {
        return merchantMapper.selectMerchantList(merchant);
    }

    @Override
    public TakeoutMerchant queryById(Long merchantId)
    {
        return merchantMapper.selectMerchantById(merchantId);
    }

    @Override
    public TakeoutMerchant queryByOwnerUserId(Long ownerUserId)
    {
        return merchantMapper.selectMerchantByOwnerId(ownerUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(TakeoutMerchant merchant)
    {
        Long ownerUserId = merchant.getOwnerUserId();
        if (ownerUserId == null)
        {
            ownerUserId = SecurityUtils.getUserId();
            merchant.setOwnerUserId(ownerUserId);
        }
        // owner_user_id 唯一校验
        TakeoutMerchant existing = merchantMapper.selectMerchantByOwnerId(ownerUserId);
        if (existing != null)
        {
            throw new ServiceException("该用户已绑定商家，不可重复创建");
        }
        merchant.setCreateBy(SecurityUtils.getUsername());
        if (merchant.getStatus() == null || merchant.getStatus().isEmpty())
        {
            merchant.setStatus("0"); // 默认营业
        }
        return merchantMapper.insertMerchant(merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TakeoutMerchant merchant)
    {
        Long merchantId = merchant.getMerchantId();
        TakeoutMerchant db = merchantMapper.selectMerchantById(merchantId);
        if (db == null)
        {
            throw new ServiceException("商家不存在");
        }
        // owner_user_id 唯一校验（如果要修改 ownerUserId）
        Long newOwner = merchant.getOwnerUserId();
        if (newOwner != null && !newOwner.equals(db.getOwnerUserId()))
        {
            TakeoutMerchant conflict = merchantMapper.selectMerchantByOwnerId(newOwner);
            if (conflict != null)
            {
                throw new ServiceException("目标用户已绑定商家，不可转移");
            }
        }
        merchant.setUpdateBy(SecurityUtils.getUsername());
        if (merchant.getVersion() == null)
        {
            merchant.setVersion(db.getVersion());
        }
        int rows = merchantMapper.updateMerchant(merchant);
        if (rows == 0)
        {
            throw new ServiceException("商家信息已变更，请刷新后重试");
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long merchantId)
    {
        return merchantMapper.deleteMerchantById(merchantId);
    }
}
