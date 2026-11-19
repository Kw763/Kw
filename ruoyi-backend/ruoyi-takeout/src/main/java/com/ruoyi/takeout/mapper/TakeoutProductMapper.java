package com.ruoyi.takeout.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.takeout.domain.TakeoutProduct;

public interface TakeoutProductMapper
{
    List<TakeoutProduct> selectProductList(TakeoutProduct query);
    TakeoutProduct selectProductById(Long productId);
    TakeoutProduct selectActiveProductById(Long productId);
    int insertProduct(TakeoutProduct product);
    int updateProduct(TakeoutProduct product);
    int updateProductStatus(@Param("productId") Long productId, @Param("status") String status,
            @Param("version") Integer version, @Param("updateBy") String updateBy);
    int deleteProductById(Long productId);
}
