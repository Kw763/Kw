package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.dto.ProductStatusRequest;

/**
 * 外卖商品 业务层
 *
 * @author ruoyi
 */
public interface TakeoutProductService
{
    List<TakeoutProduct> list(TakeoutProduct product);

    TakeoutProduct queryById(Long productId);

    int save(TakeoutProduct product);

    int update(TakeoutProduct product);

    int setStatus(Long productId, ProductStatusRequest request);

    int delete(Long productId);
}
