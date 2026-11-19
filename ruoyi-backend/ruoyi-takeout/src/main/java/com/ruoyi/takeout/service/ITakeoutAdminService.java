package com.ruoyi.takeout.service;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.takeout.domain.TakeoutAddress;
import com.ruoyi.takeout.domain.TakeoutDeliveryTask;
import com.ruoyi.takeout.domain.TakeoutMerchant;
import com.ruoyi.takeout.domain.TakeoutOrder;
import com.ruoyi.takeout.domain.TakeoutProduct;
import com.ruoyi.takeout.domain.TakeoutStatusLog;

/**
 * 外卖后台管理聚合 Service 接口
 *
 * @author ruoyi
 */
public interface ITakeoutAdminService
{
    // ==================== 商家管理 ====================

    List<TakeoutMerchant> selectMerchantList(TakeoutMerchant query);

    TakeoutMerchant selectMerchantById(Long merchantId);

    int insertMerchant(TakeoutMerchant merchant);

    int updateMerchant(TakeoutMerchant merchant);

    int deleteMerchant(Long merchantId);

    // ==================== 商品管理 ====================

    List<TakeoutProduct> selectProductList(TakeoutProduct query);

    TakeoutProduct selectProductById(Long productId);

    int insertProduct(TakeoutProduct product);

    int updateProduct(TakeoutProduct product);

    int deleteProduct(Long productId);

    // ==================== 地址查询 ====================

    List<TakeoutAddress> selectAddressList(TakeoutAddress query);

    // ==================== 订单管理 ====================

    List<TakeoutOrder> selectOrderList(TakeoutOrder query);

    TakeoutOrder selectOrderById(Long orderId);

    // ==================== 配送任务 ====================

    List<TakeoutDeliveryTask> selectDeliveryTaskList(TakeoutDeliveryTask query);

    int assignDelivery(Long taskId, Long riderId);

    // ==================== 状态日志 ====================

    List<TakeoutStatusLog> selectStatusLogList(TakeoutStatusLog query);

    // ==================== 骑手列表 ====================

    List<SysUser> selectAvailableRiders();
}
