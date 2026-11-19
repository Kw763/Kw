package com.ruoyi.takeout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.takeout.domain.TakeoutMerchant;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.dto.ProductStatusRequest;
import com.ruoyi.takeout.mapper.TakeoutMerchantMapper;
import com.ruoyi.takeout.mapper.TakeoutProductMapper;
import com.ruoyi.takeout.service.TakeoutProductService;

/**
 * 外卖商品 业务层实现
 *
 * @author ruoyi
 */
@Service
public class TakeoutProductServiceImpl implements TakeoutProductService
{
    @Autowired
    private TakeoutProductMapper productMapper;

    @Autowired
    private TakeoutMerchantMapper merchantMapper;

    /**
     * 校验登录用户是否是指定 merchant 的 owner，并返回该 merchant。
     */
    private TakeoutMerchant validateMerchantOwner(Long merchantId)
    {
        if (merchantId == null)
        {
            throw new ServiceException("商家ID不能为空");
        }
        TakeoutMerchant merchant = merchantMapper.selectMerchantById(merchantId);
        if (merchant == null)
        {
            throw new ServiceException("商家不存在");
        }
        Long userId = SecurityUtils.getUserId();
        if (!userId.equals(merchant.getOwnerUserId()))
        {
            throw new ServiceException("无权操作该商家的商品");
        }
        return merchant;
    }

    @Override
    public List<TakeoutProduct> list(TakeoutProduct product)
    {
        return productMapper.selectProductList(product);
    }

    @Override
    public TakeoutProduct queryById(Long productId)
    {
        return productMapper.selectProductById(productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(TakeoutProduct product)
    {
        Long merchantId = product.getMerchantId();
        if (merchantId == null)
        {
            // 没传 merchantId，自动取当前登录商家
            TakeoutMerchant merchant = merchantMapper.selectMerchantByOwnerId(SecurityUtils.getUserId());
            if (merchant == null)
            {
                throw new ServiceException("当前用户未绑定商家");
            }
            merchantId = merchant.getMerchantId();
            product.setMerchantId(merchantId);
        }
        else
        {
            validateMerchantOwner(merchantId);
        }
        product.setCreateBy(SecurityUtils.getUsername());
        if (product.getStatus() == null || product.getStatus().isEmpty())
        {
            product.setStatus("0"); // 默认上架
        }
        return productMapper.insertProduct(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TakeoutProduct product)
    {
        Long productId = product.getProductId();
        TakeoutProduct db = productMapper.selectProductById(productId);
        if (db == null)
        {
            throw new ServiceException("商品不存在");
        }
        validateMerchantOwner(db.getMerchantId());
        // 如果修改 merchantId，也需要校验新 merchant 的归属
        if (product.getMerchantId() != null && !product.getMerchantId().equals(db.getMerchantId()))
        {
            validateMerchantOwner(product.getMerchantId());
        }
        product.setUpdateBy(SecurityUtils.getUsername());
        if (product.getVersion() == null)
        {
            product.setVersion(db.getVersion());
        }
        int rows = productMapper.updateProduct(product);
        if (rows == 0)
        {
            throw new ServiceException("商品信息已变更，请刷新后重试");
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int setStatus(Long productId, ProductStatusRequest request)
    {
        TakeoutProduct db = productMapper.selectProductById(productId);
        if (db == null)
        {
            throw new ServiceException("商品不存在");
        }
        validateMerchantOwner(db.getMerchantId());

        String updateBy = SecurityUtils.getUsername();
        int rows = productMapper.updateProductStatus(productId, request.status(), db.getVersion(), updateBy);
        if (rows == 0)
        {
            throw new ServiceException("商品状态已变更，请刷新后重试");
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long productId)
    {
        TakeoutProduct db = productMapper.selectProductById(productId);
        if (db == null)
        {
            throw new ServiceException("商品不存在");
        }
        validateMerchantOwner(db.getMerchantId());
        return productMapper.deleteProductById(productId);
    }
}
