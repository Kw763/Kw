package com.ruoyi.takeout.domain;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外卖订单对象 takeout_order
 *
 * @author ruoyi
 */
public class TakeoutOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @Excel(name = "订单ID", cellType = ColumnType.NUMERIC)
    private Long orderId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 用户请求号 */
    private String requestNo;

    /** 下单用户ID */
    private Long userId;

    /** 下单用户名称（关联查询） */
    private String userName;

    /** 商家ID */
    private Long merchantId;

    /** 商家名称（关联查询） */
    @Excel(name = "商家")
    private String merchantName;

    /** 收货人快照 */
    @Excel(name = "收货人")
    private String receiverName;

    /** 收货电话快照 */
    @Excel(name = "收货电话")
    private String receiverPhone;

    /** 完整收货地址快照 */
    @Excel(name = "收货地址")
    private String deliveryAddress;

    /** 订单总金额 */
    @Excel(name = "订单金额")
    private BigDecimal totalAmount;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String status;

    /** 乐观锁版本 */
    private Integer version;

    /** 订单明细列表 */
    private List<TakeoutOrderItem> items;

    /** 支付单（关联查询） */
    private TakeoutPayment payment;

    /** 配送任务（关联查询） */
    private TakeoutDeliveryTask deliveryTask;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    @NotBlank(message = "订单号不能为空")
    @Size(min = 0, max = 64, message = "订单号长度不能超过64个字符")
    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    @NotBlank(message = "用户请求号不能为空")
    @Size(min = 0, max = 64, message = "用户请求号长度不能超过64个字符")
    public String getRequestNo()
    {
        return requestNo;
    }

    public void setRequestNo(String requestNo)
    {
        this.requestNo = requestNo;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Long getMerchantId()
    {
        return merchantId;
    }

    public void setMerchantId(Long merchantId)
    {
        this.merchantId = merchantId;
    }

    public String getMerchantName()
    {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    @NotBlank(message = "收货人不能为空")
    @Size(min = 0, max = 50, message = "收货人长度不能超过50个字符")
    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    @NotBlank(message = "收货电话不能为空")
    @Size(min = 0, max = 20, message = "收货电话长度不能超过20个字符")
    public String getReceiverPhone()
    {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone)
    {
        this.receiverPhone = receiverPhone;
    }

    @NotBlank(message = "收货地址不能为空")
    @Size(min = 0, max = 500, message = "收货地址长度不能超过500个字符")
    public String getDeliveryAddress()
    {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }

    @NotNull(message = "订单总金额不能为空")
    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public List<TakeoutOrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<TakeoutOrderItem> items)
    {
        this.items = items;
    }

    public TakeoutPayment getPayment()
    {
        return payment;
    }

    public void setPayment(TakeoutPayment payment)
    {
        this.payment = payment;
    }

    public TakeoutDeliveryTask getDeliveryTask()
    {
        return deliveryTask;
    }

    public void setDeliveryTask(TakeoutDeliveryTask deliveryTask)
    {
        this.deliveryTask = deliveryTask;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("requestNo", getRequestNo())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("merchantId", getMerchantId())
            .append("merchantName", getMerchantName())
            .append("receiverName", getReceiverName())
            .append("receiverPhone", getReceiverPhone())
            .append("deliveryAddress", getDeliveryAddress())
            .append("totalAmount", getTotalAmount())
            .append("status", getStatus())
            .append("version", getVersion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
