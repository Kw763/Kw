package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.dto.AddressSaveRequest;

/**
 * 用户收货地址 业务层
 *
 * @author ruoyi
 */
public interface TakeoutAddressService
{
    List<TakeoutAddress> list(TakeoutAddress address);

    TakeoutAddress queryById(Long addressId);

    int save(AddressSaveRequest request);

    int update(Long addressId, AddressSaveRequest request);

    int delete(Long addressId);

    int setDefault(Long addressId);
}
