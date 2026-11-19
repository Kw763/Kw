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
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.dto.AddressSaveRequest;
import com.ruoyi.takeout.domain.dto.CreateOrderRequest;
import com.ruoyi.takeout.service.ITakeoutUserService;

/**
 * 外卖用户端控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/takeout/user")
public class TakeoutUserController extends BaseController
{
    @Autowired
    private ITakeoutUserService takeoutUserService;

    // ==================== 地址管理 ====================

    /**
     * 查询收货地址列表
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:address:list')")
    @GetMapping("/address/list")
    public TableDataInfo addressList()
    {
        startPage();
        List<TakeoutAddress> list = takeoutUserService.selectMyAddressList();
        return getDataTable(list);
    }

    /**
     * 查询收货地址详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:address:query')")
    @GetMapping("/address/{addressId}")
    public AjaxResult addressDetail(@PathVariable Long addressId)
    {
        return success(takeoutUserService.selectAddressById(addressId));
    }

    /**
     * 新增收货地址
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:address:add')")
    @Log(title = "外卖-收货地址", businessType = BusinessType.INSERT)
    @PostMapping("/address")
    public AjaxResult addAddress(@Valid @RequestBody AddressSaveRequest request)
    {
        return toAjax(takeoutUserService.insertAddress(request));
    }

    /**
     * 修改收货地址
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:address:edit')")
    @Log(title = "外卖-收货地址", businessType = BusinessType.UPDATE)
    @PutMapping("/address/{addressId}")
    public AjaxResult updateAddress(@PathVariable Long addressId,
            @Valid @RequestBody AddressSaveRequest request)
    {
        return toAjax(takeoutUserService.updateAddress(addressId, request));
    }

    /**
     * 删除收货地址
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:address:remove')")
    @Log(title = "外卖-收货地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/address/{addressId}")
    public AjaxResult removeAddress(@PathVariable Long addressId)
    {
        return toAjax(takeoutUserService.deleteAddress(addressId));
    }

    /**
     * 设为默认地址
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:address:default')")
    @Log(title = "外卖-收货地址", businessType = BusinessType.UPDATE)
    @PutMapping("/address/{addressId}/setDefault")
    public AjaxResult setDefaultAddress(@PathVariable Long addressId)
    {
        return toAjax(takeoutUserService.setDefaultAddress(addressId));
    }

    // ==================== 订单管理 ====================

    /**
     * 创建订单
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:order:add')")
    @Log(title = "外卖-用户订单", businessType = BusinessType.INSERT)
    @PostMapping("/orders")
    public AjaxResult createOrder(@Valid @RequestBody CreateOrderRequest request)
    {
        return success(takeoutUserService.createOrder(request));
    }

    /**
     * 本人订单列表
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:order:list')")
    @GetMapping("/orders")
    public TableDataInfo orderList(TakeoutOrder query)
    {
        startPage();
        List<TakeoutOrder> list = takeoutUserService.selectMyOrderList(query);
        return getDataTable(list);
    }

    /**
     * 本人订单详情
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:order:query')")
    @GetMapping("/orders/{orderId}")
    public AjaxResult orderDetail(@PathVariable Long orderId)
    {
        return success(takeoutUserService.selectOrderDetail(orderId));
    }

    /**
     * 模拟支付（仅允许订单本人）
     */
    @PreAuthorize("@ss.hasPermi('takeout:user:order:pay')")
    @Log(title = "外卖-模拟支付", businessType = BusinessType.UPDATE)
    @PostMapping("/orders/{orderId}/mockPay")
    public AjaxResult mockPay(@PathVariable Long orderId)
    {
        return toAjax(takeoutUserService.mockPay(orderId));
    }
}
