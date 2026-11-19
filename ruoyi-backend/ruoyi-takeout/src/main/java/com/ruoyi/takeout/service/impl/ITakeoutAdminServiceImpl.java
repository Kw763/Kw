package com.ruoyi.takeout.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.domain.TakeoutMerchant;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.TakeoutStatusLog;
import com.ruoyi.takeout.domain.dto.AssignDeliveryRequest;
import com.ruoyi.takeout.mapper.TakeoutDeliveryMapper;
import com.ruoyi.takeout.mapper.TakeoutStatusLogMapper;
import com.ruoyi.takeout.service.ITakeoutAdminService;
import com.ruoyi.takeout.service.TakeoutAddressService;
import com.ruoyi.takeout.service.TakeoutDeliveryService;
import com.ruoyi.takeout.service.TakeoutMerchantService;
import com.ruoyi.takeout.service.TakeoutOrderService;
import com.ruoyi.takeout.service.TakeoutProductService;

/**
 * 外卖后台管理聚合 Service 实现
 *
 * @author ruoyi
 */
@Service
public class ITakeoutAdminServiceImpl implements ITakeoutAdminService
{
    @Autowired
    private TakeoutMerchantService merchantService;

    @Autowired
    private TakeoutProductService productService;

    @Autowired
    private TakeoutAddressService addressService;

    @Autowired
    private TakeoutOrderService orderService;

    @Autowired
    private TakeoutDeliveryService deliveryService;

    @Autowired
    private TakeoutDeliveryMapper deliveryMapper;

    @Autowired
    private TakeoutStatusLogMapper statusLogMapper;

    @Autowired
    private ISysUserService userService;

    // ==================== 商家管理 ====================

    @Override
    public List<TakeoutMerchant> selectMerchantList(TakeoutMerchant query)
    {
        return merchantService.list(query);
    }

    @Override
    public TakeoutMerchant selectMerchantById(Long merchantId)
    {
        return merchantService.queryById(merchantId);
    }

    @Override
    public int insertMerchant(TakeoutMerchant merchant)
    {
        return merchantService.save(merchant);
    }

    @Override
    public int updateMerchant(TakeoutMerchant merchant)
    {
        return merchantService.update(merchant);
    }

    @Override
    public int deleteMerchant(Long merchantId)
    {
        return merchantService.delete(merchantId);
    }

    // ==================== 商品管理 ====================

    @Override
    public List<TakeoutProduct> selectProductList(TakeoutProduct query)
    {
        return productService.list(query);
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
    public int deleteProduct(Long productId)
    {
        return productService.delete(productId);
    }

    // ==================== 地址查询 ====================

    @Override
    public List<TakeoutAddress> selectAddressList(TakeoutAddress query)
    {
        return addressService.list(query);
    }

    // ==================== 订单管理 ====================

    @Override
    public List<TakeoutOrder> selectOrderList(TakeoutOrder query)
    {
        return orderService.listForAdmin(query);
    }

    @Override
    public TakeoutOrder selectOrderById(Long orderId)
    {
        return orderService.detail(orderId);
    }

    // ==================== 配送任务 ====================

    @Override
    public List<TakeoutDeliveryTask> selectDeliveryTaskList(TakeoutDeliveryTask query)
    {
        return deliveryMapper.selectDeliveryList(query);
    }

    @Override
    public int assignDelivery(Long taskId, Long riderId)
    {
        return deliveryService.assign(taskId, new AssignDeliveryRequest(riderId));
    }

    // ==================== 状态日志 ====================

    @Override
    public List<TakeoutStatusLog> selectStatusLogList(TakeoutStatusLog query)
    {
        return statusLogMapper.selectStatusLogList(query);
    }

    // ==================== 骑手列表 ====================

    @Override
    public List<SysUser> selectAvailableRiders()
    {
        SysUser query = new SysUser();
        query.setStatus("0");
        List<SysUser> enabledUsers = userService.selectUserList(query);
        List<SysUser> riders = new ArrayList<>();
        for (SysUser user : enabledUsers)
        {
            SysUser full = userService.selectUserById(user.getUserId());
            if (full != null && full.getRoles() != null)
            {
                for (SysRole role : full.getRoles())
                {
                    if ("takeout_rider".equals(role.getRoleKey()))
                    {
                        riders.add(full);
                        break;
                    }
                }
            }
        }
        return riders;
    }
}
