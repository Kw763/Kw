package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutProduct;

/**
 * 外卖商家端聚合 Service 接口
 *
 * @author ruoyi
 */
public interface ITakeoutMerchantService
{
    // ==================== 商品管理 ====================

    List<TakeoutProduct> selectProductList(TakeoutProduct product);

    TakeoutProduct selectProductById(Long productId);

    int insertProduct(TakeoutProduct product);

    int updateProduct(TakeoutProduct product);

    int updateProductStatus(Long productId, String status);

    int deleteProduct(Long productId);

    // ==================== 订单管理 ====================

    List<TakeoutOrder> selectOrderList(TakeoutOrder order);

    TakeoutOrder selectOrderById(Long orderId);

    int acceptOrder(Long orderId);

    int markOrderReady(Long orderId);
}
