package com.ruoyi.takeout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.dto.ProductStatusRequest;
import com.ruoyi.takeout.service.ITakeoutMerchantService;
import com.ruoyi.takeout.service.TakeoutOrderService;
import com.ruoyi.takeout.service.TakeoutProductService;

/**
 * 外卖商家端聚合 Service 实现
 *
 * @author ruoyi
 */
@Service
public class ITakeoutMerchantServiceImpl implements ITakeoutMerchantService
{
    @Autowired
    private TakeoutProductService productService;

    @Autowired
    private TakeoutOrderService orderService;

    // ==================== 商品管理 ====================

    @Override
    public List<TakeoutProduct> selectProductList(TakeoutProduct product)
    {
        return productService.list(product);
    }

    @Override
    public TakeoutProduct selectProductById(Long productId)
    {
        return productService.queryById(productId);
    }

    @Override
    public int insertProduct(TakeoutProduct product)
    {
        return productService.save(product);
    }

    @Override
    public int updateProduct(TakeoutProduct product)
    {
        return productService.update(product);
    }

    @Override
    public int updateProductStatus(Long productId, String status)
    {
        return productService.setStatus(productId, new ProductStatusRequest(status));
    }

    @Override
    public int deleteProduct(Long productId)
    {
        return productService.delete(productId);
    }

    // ==================== 订单管理 ====================

    @Override
    public List<TakeoutOrder> selectOrderList(TakeoutOrder order)
    {
        return orderService.listForMerchant(order);
    }

    @Override
    public TakeoutOrder selectOrderById(Long orderId)
    {
        return orderService.detail(orderId);
    }

    @Override
    public int acceptOrder(Long orderId)
    {
        return orderService.accept(orderId);
    }

    @Override
    public int markOrderReady(Long orderId)
    {
        return orderService.markReady(orderId);
    }
}
