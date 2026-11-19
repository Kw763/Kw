package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.dto.AddressSaveRequest;
import com.ruoyi.takeout.domain.dto.CreateOrderRequest;

/**
 * 外卖用户端聚合 Service 接口
 *
 * @author ruoyi
 */
public interface ITakeoutUserService
{
    // ==================== 地址管理 ====================

    List<TakeoutAddress> selectMyAddressList();

    TakeoutAddress selectAddressById(Long addressId);

    int insertAddress(AddressSaveRequest request);

    int updateAddress(Long addressId, AddressSaveRequest request);

    int deleteAddress(Long addressId);

    int setDefaultAddress(Long addressId);

    // ==================== 订单管理 ====================

    TakeoutOrder createOrder(CreateOrderRequest request);

    List<TakeoutOrder> selectMyOrderList(TakeoutOrder query);

    TakeoutOrder selectOrderDetail(Long orderId);

    int mockPay(Long orderId);
}
