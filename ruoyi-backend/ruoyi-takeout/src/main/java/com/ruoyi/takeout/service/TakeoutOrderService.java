package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.dto.CreateOrderRequest;

/**
 * 外卖订单 业务层
 *
 * @author ruoyi
 */
public interface TakeoutOrderService
{
    TakeoutOrder create(CreateOrderRequest request);

    int mockPay(Long orderId);

    List<TakeoutOrder> listForUser(TakeoutOrder order);

    List<TakeoutOrder> listForMerchant(TakeoutOrder order);

    List<TakeoutOrder> listForAdmin(TakeoutOrder order);

    TakeoutOrder detail(Long orderId);

    int accept(Long orderId);

    int markReady(Long orderId);
}
