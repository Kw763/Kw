package com.ruoyi.web.controller.takeout;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.dto.ProductStatusRequest;
import com.ruoyi.takeout.service.ITakeoutMerchantService;

/**
 * 外卖商家端控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/takeout/merchant")
public class TakeoutMerchantController extends BaseController
{
    @Autowired
    private ITakeoutMerchantService takeoutMerchantService;

    // ==================== 商品管理 ====================

    /**
     * 查询商品列表（Service 内部按 owner_user_id 过滤）
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:product:list')")
    @GetMapping("/product/list")
    public TableDataInfo productList(TakeoutProduct product)
    {
        startPage();
        List<TakeoutProduct> list = takeoutMerchantService.selectProductList(product);
        return getDataTable(list);
    }

    /**
     * 查询商品详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:product:query')")
    @GetMapping("/product/{productId}")
    public AjaxResult productDetail(@PathVariable Long productId)
    {
        return success(takeoutMerchantService.selectProductById(productId));
    }

    /**
     * 新增商品
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:product:add')")
    @Log(title = "外卖-商家商品", businessType = BusinessType.INSERT)
    @PostMapping("/product")
    public AjaxResult addProduct(@Valid @RequestBody TakeoutProduct product)
    {
        return toAjax(takeoutMerchantService.insertProduct(product));
    }

    /**
     * 修改商品
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:product:edit')")
    @Log(title = "外卖-商家商品", businessType = BusinessType.UPDATE)
    @PutMapping("/product")
    public AjaxResult updateProduct(@Valid @RequestBody TakeoutProduct product)
    {
        return toAjax(takeoutMerchantService.updateProduct(product));
    }

    /**
     * 商品上下架
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:product:status')")
    @Log(title = "外卖-商品状态", businessType = BusinessType.UPDATE)
    @PutMapping("/product/{productId}/setStatus")
    public AjaxResult setProductStatus(@PathVariable Long productId,
            @Valid @RequestBody ProductStatusRequest request)
    {
        return toAjax(takeoutMerchantService.updateProductStatus(productId, request.status()));
    }

    /**
     * 删除商品
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:product:remove')")
    @Log(title = "外卖-商家商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/product/{productId}")
    public AjaxResult removeProduct(@PathVariable Long productId)
    {
        return toAjax(takeoutMerchantService.deleteProduct(productId));
    }

    // ==================== 订单管理 ====================

    /**
     * 查询本商家订单列表（Service 内部按 owner_user_id 过滤）
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:order:list')")
    @GetMapping("/order/list")
    public TableDataInfo orderList(TakeoutOrder order)
    {
        startPage();
        List<TakeoutOrder> list = takeoutMerchantService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 查询本商家订单详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:order:query')")
    @GetMapping("/order/{orderId}")
    public AjaxResult orderDetail(@PathVariable Long orderId)
    {
        return success(takeoutMerchantService.selectOrderById(orderId));
    }

    /**
     * 商家接单
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:order:accept')")
    @Log(title = "外卖-商家接单", businessType = BusinessType.UPDATE)
    @PostMapping("/order/{orderId}/accept")
    public AjaxResult acceptOrder(@PathVariable Long orderId)
    {
        return toAjax(takeoutMerchantService.acceptOrder(orderId));
    }

    /**
     * 商家制作完成，标记备餐完成
     */
    @PreAuthorize("@ss.hasPermi('takeout:merchant:order:ready')")
    @Log(title = "外卖-商家备餐", businessType = BusinessType.UPDATE)
    @PostMapping("/order/{orderId}/markReady")
    public AjaxResult markOrderReady(@PathVariable Long orderId)
    {
        return toAjax(takeoutMerchantService.markOrderReady(orderId));
    }
}
