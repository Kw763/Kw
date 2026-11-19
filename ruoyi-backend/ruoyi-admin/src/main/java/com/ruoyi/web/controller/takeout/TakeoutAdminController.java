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
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.domain.TakeoutMerchant;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.TakeoutStatusLog;
import com.ruoyi.takeout.domain.dto.AssignDeliveryRequest;
import com.ruoyi.takeout.service.ITakeoutAdminService;

/**
 * 外卖后台管理控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/takeout/admin")
public class TakeoutAdminController extends BaseController
{
    @Autowired
    private ITakeoutAdminService takeoutAdminService;

    // ==================== 商家管理 ====================

    /**
     * 商家列表
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:list')")
    @GetMapping("/merchant/list")
    public TableDataInfo merchantList(TakeoutMerchant merchant)
    {
        startPage();
        List<TakeoutMerchant> list = takeoutAdminService.selectMerchantList(merchant);
        return getDataTable(list);
    }

    /**
     * 商家详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:query')")
    @GetMapping("/merchant/{merchantId}")
    public AjaxResult merchantDetail(@PathVariable Long merchantId)
    {
        return success(takeoutAdminService.selectMerchantById(merchantId));
    }

    /**
     * 新增商家
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:add')")
    @Log(title = "外卖-商家管理", businessType = BusinessType.INSERT)
    @PostMapping("/merchant")
    public AjaxResult addMerchant(@Valid @RequestBody TakeoutMerchant merchant)
    {
        return toAjax(takeoutAdminService.insertMerchant(merchant));
    }

    /**
     * 修改商家
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:edit')")
    @Log(title = "外卖-商家管理", businessType = BusinessType.UPDATE)
    @PutMapping("/merchant")
    public AjaxResult updateMerchant(@Valid @RequestBody TakeoutMerchant merchant)
    {
        return toAjax(takeoutAdminService.updateMerchant(merchant));
    }

    /**
     * 删除商家
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:remove')")
    @Log(title = "外卖-商家管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/merchant/{merchantId}")
    public AjaxResult removeMerchant(@PathVariable Long merchantId)
    {
        return toAjax(takeoutAdminService.deleteMerchant(merchantId));
    }

    // ==================== 商品管理 ====================

    /**
     * 商品列表
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:list')")
    @GetMapping("/product/list")
    public TableDataInfo productList(TakeoutProduct product)
    {
        startPage();
        List<TakeoutProduct> list = takeoutAdminService.selectProductList(product);
        return getDataTable(list);
    }

    /**
     * 商品详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:query')")
    @GetMapping("/product/{productId}")
    public AjaxResult productDetail(@PathVariable Long productId)
    {
        return success(takeoutAdminService.selectProductById(productId));
    }

    /**
     * 新增商品
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:add')")
    @Log(title = "外卖-商品管理", businessType = BusinessType.INSERT)
    @PostMapping("/product")
    public AjaxResult addProduct(@Valid @RequestBody TakeoutProduct product)
    {
        return toAjax(takeoutAdminService.insertProduct(product));
    }

    /**
     * 修改商品
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:edit')")
    @Log(title = "外卖-商品管理", businessType = BusinessType.UPDATE)
    @PutMapping("/product")
    public AjaxResult updateProduct(@Valid @RequestBody TakeoutProduct product)
    {
        return toAjax(takeoutAdminService.updateProduct(product));
    }

    /**
     * 删除商品
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:remove')")
    @Log(title = "外卖-商品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/product/{productId}")
    public AjaxResult removeProduct(@PathVariable Long productId)
    {
        return toAjax(takeoutAdminService.deleteProduct(productId));
    }

    // ==================== 地址查询 ====================

    /**
     * 地址列表查询
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:query')")
    @GetMapping("/address/list")
    public TableDataInfo addressList(TakeoutAddress address)
    {
        startPage();
        List<TakeoutAddress> list = takeoutAdminService.selectAddressList(address);
        return getDataTable(list);
    }

    // ==================== 订单管理 ====================

    /**
     * 全量订单列表
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:list')")
    @GetMapping("/order/list")
    public TableDataInfo orderList(TakeoutOrder order)
    {
        startPage();
        List<TakeoutOrder> list = takeoutAdminService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 订单详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:query')")
    @GetMapping("/order/{orderId}")
    public AjaxResult orderDetail(@PathVariable Long orderId)
    {
        return success(takeoutAdminService.selectOrderById(orderId));
    }

    // ==================== 配送任务 ====================

    /**
     * 配送任务列表
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:list')")
    @GetMapping("/delivery/list")
    public TableDataInfo deliveryList(TakeoutDeliveryTask task)
    {
        startPage();
        List<TakeoutDeliveryTask> list = takeoutAdminService.selectDeliveryTaskList(task);
        return getDataTable(list);
    }

    /**
     * 手动派单
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:edit')")
    @Log(title = "外卖-配送派单", businessType = BusinessType.UPDATE)
    @PutMapping("/delivery/{taskId}/assign")
    public AjaxResult assignDelivery(@PathVariable Long taskId,
            @Valid @RequestBody AssignDeliveryRequest request)
    {
        return toAjax(takeoutAdminService.assignDelivery(taskId, request.riderId()));
    }

    // ==================== 状态日志 ====================

    /**
     * 状态日志查询
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:query')")
    @GetMapping("/statusLog/list")
    public TableDataInfo statusLogList(TakeoutStatusLog log)
    {
        startPage();
        List<TakeoutStatusLog> list = takeoutAdminService.selectStatusLogList(log);
        return getDataTable(list);
    }

    // ==================== 骑手列表 ====================

    /**
     * 可选骑手列表（启用且有 takeout_rider 角色的 sys_user）
     */
    @PreAuthorize("@ss.hasPermi('takeout:admin:query')")
    @GetMapping("/rider/available")
    public AjaxResult availableRiders()
    {
        List<SysUser> list = takeoutAdminService.selectAvailableRiders();
        return success(list);
    }
}
