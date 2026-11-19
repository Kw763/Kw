package com.ruoyi.takeout.domain;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外卖配送任务对象 takeout_delivery_task
 *
 * @author ruoyi
 */
public class TakeoutDeliveryTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配送任务ID */
    @Excel(name = "任务ID", cellType = ColumnType.NUMERIC)
    private Long taskId;

    /** 配送任务号 */
    @Excel(name = "任务号")
    private String taskNo;

    /** 订单ID */
    private Long orderId;

    /** 订单号（关联查询） */
    @Excel(name = "订单号")
    private String orderNo;

    /** 骑手用户ID */
    private Long riderId;

    /** 骑手姓名（关联查询） */
    @Excel(name = "骑手")
    private String riderName;

    /** 配送状态 */
    @Excel(name = "配送状态")
    private String status;

    /** 商家名称（关联查询快照） */
    @Excel(name = "商家")
    private String merchantName;

    /** 商家地址（关联查询快照） */
    private String merchantAddress;

    /** 收货人（关联查询快照） */
    @Excel(name = "收货人")
    private String receiverName;

    /** 收货电话（关联查询快照） */
    @Excel(name = "收货电话")
    private String receiverPhone;

    /** 完整收货地址（关联查询快照） */
    private String deliveryAddress;

    /** 接单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "接单时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date acceptTime;

    /** 到店时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "到店时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date arriveTime;

    /** 取餐时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "取餐时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date pickupTime;

    /** 送达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "送达时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deliverTime;

    /** 乐观锁版本 */
    private Integer version;

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    @NotBlank(message = "配送任务号不能为空")
    @Size(min = 0, max = 64, message = "配送任务号长度不能超过64个字符")
    public String getTaskNo()
    {
        return taskNo;
    }

    public void setTaskNo(String taskNo)
    {
        this.taskNo = taskNo;
    }

    @NotNull(message = "订单ID不能为空")
    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Long getRiderId()
    {
        return riderId;
    }

    public void setRiderId(Long riderId)
    {
        this.riderId = riderId;
    }

    public String getRiderName()
    {
        return riderName;
    }

    public void setRiderName(String riderName)
    {
        this.riderName = riderName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMerchantName()
    {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    public String getMerchantAddress()
    {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress)
    {
        this.merchantAddress = merchantAddress;
    }

    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone()
    {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone)
    {
        this.receiverPhone = receiverPhone;
    }

    public String getDeliveryAddress()
    {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getAcceptTime()
    {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime)
    {
        this.acceptTime = acceptTime;
    }

    public Date getArriveTime()
    {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime)
    {
        this.arriveTime = arriveTime;
    }

    public Date getPickupTime()
    {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime)
    {
        this.pickupTime = pickupTime;
    }

    public Date getDeliverTime()
    {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime)
    {
        this.deliverTime = deliverTime;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("taskNo", getTaskNo())
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("riderId", getRiderId())
            .append("riderName", getRiderName())
            .append("status", getStatus())
            .append("merchantName", getMerchantName())
            .append("merchantAddress", getMerchantAddress())
            .append("receiverName", getReceiverName())
            .append("receiverPhone", getReceiverPhone())
            .append("deliveryAddress", getDeliveryAddress())
            .append("acceptTime", getAcceptTime())
            .append("arriveTime", getArriveTime())
            .append("pickupTime", getPickupTime())
            .append("deliverTime", getDeliverTime())
            .append("version", getVersion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
