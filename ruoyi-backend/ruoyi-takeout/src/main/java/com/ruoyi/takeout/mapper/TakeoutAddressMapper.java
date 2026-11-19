package com.ruoyi.takeout.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.takeout.domain.TakeoutAddress;

public interface TakeoutAddressMapper
{
    List<TakeoutAddress> selectAddressList(TakeoutAddress query);
    TakeoutAddress selectAddressById(Long addressId);
    TakeoutAddress selectAddressByIdAndUserId(@Param("addressId") Long addressId, @Param("userId") Long userId);
    int insertAddress(TakeoutAddress address);
    int updateAddress(TakeoutAddress address);
    int clearDefaultAddress(@Param("userId") Long userId, @Param("updateBy") String updateBy);
    int setDefaultAddress(@Param("addressId") Long addressId, @Param("userId") Long userId,
            @Param("updateBy") String updateBy);
    int deleteAddressByIdAndUserId(@Param("addressId") Long addressId, @Param("userId") Long userId,
            @Param("updateBy") String updateBy);
    int deleteAddressById(Long addressId);
}
