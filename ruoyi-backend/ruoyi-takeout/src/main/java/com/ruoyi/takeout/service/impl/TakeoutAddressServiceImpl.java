package com.ruoyi.takeout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.dto.AddressSaveRequest;
import com.ruoyi.takeout.mapper.TakeoutAddressMapper;
import com.ruoyi.takeout.service.TakeoutAddressService;

/**
 * 用户收货地址 业务层实现
 *
 * @author ruoyi
 */
@Service
public class TakeoutAddressServiceImpl implements TakeoutAddressService
{
    @Autowired
    private TakeoutAddressMapper addressMapper;

    @Override
    public List<TakeoutAddress> list(TakeoutAddress address)
    {
        address.setUserId(SecurityUtils.getUserId());
        return addressMapper.selectAddressList(address);
    }

    @Override
    public TakeoutAddress queryById(Long addressId)
    {
        return addressMapper.selectAddressByIdAndUserId(addressId, SecurityUtils.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(AddressSaveRequest request)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutAddress address = new TakeoutAddress();
        address.setUserId(userId);
        address.setReceiverName(request.receiverName());
        address.setReceiverPhone(request.receiverPhone());
        address.setProvince(request.province());
        address.setCity(request.city());
        address.setDistrict(request.district());
        address.setDetailAddress(request.detailAddress());
        address.setRemark(request.remark());
        address.setCreateBy(username);

        boolean makeDefault = "Y".equals(request.isDefault());
        if (makeDefault)
        {
            addressMapper.clearDefaultAddress(userId, username);
            address.setIsDefault("Y");
        }
        else
        {
            address.setIsDefault("N");
            // 如果这是第一条地址，自动设为默认
            TakeoutAddress countQuery = new TakeoutAddress();
            countQuery.setUserId(userId);
            long total = addressMapper.selectAddressList(countQuery).size();
            if (total == 0)
            {
                address.setIsDefault("Y");
            }
        }
        return addressMapper.insertAddress(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Long addressId, AddressSaveRequest request)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutAddress db = addressMapper.selectAddressByIdAndUserId(addressId, userId);
        if (db == null)
        {
            throw new ServiceException("地址不存在或无权修改");
        }

        TakeoutAddress address = new TakeoutAddress();
        address.setAddressId(addressId);
        address.setUserId(userId);
        address.setReceiverName(request.receiverName());
        address.setReceiverPhone(request.receiverPhone());
        address.setProvince(request.province());
        address.setCity(request.city());
        address.setDistrict(request.district());
        address.setDetailAddress(request.detailAddress());
        address.setIsDefault(request.isDefault());
        address.setRemark(request.remark());
        address.setUpdateBy(username);

        boolean makeDefault = "Y".equals(request.isDefault());
        if (makeDefault)
        {
            addressMapper.clearDefaultAddress(userId, username);
        }
        int rows = addressMapper.updateAddress(address);
        if (rows == 0)
        {
            throw new ServiceException("地址更新失败");
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long addressId)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutAddress db = addressMapper.selectAddressByIdAndUserId(addressId, userId);
        if (db == null)
        {
            throw new ServiceException("地址不存在或无权删除");
        }
        return addressMapper.deleteAddressByIdAndUserId(addressId, userId, username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setDefault(Long addressId)
    {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();

        TakeoutAddress db = addressMapper.selectAddressByIdAndUserId(addressId, userId);
        if (db == null)
        {
            throw new ServiceException("地址不存在");
        }
        addressMapper.clearDefaultAddress(userId, username);
        return addressMapper.setDefaultAddress(addressId, userId, username);
    }
}
