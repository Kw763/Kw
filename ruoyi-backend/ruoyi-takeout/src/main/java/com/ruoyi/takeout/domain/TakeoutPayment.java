package com.ruoyi.takeout.domain;

import java.math.BigDecimal;
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
 * 外卖模拟支付单对象 takeout_payment
 *
 * @author ruoyi
 */
public class TakeoutPayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 支付ID */
    @Excel(name = "支付ID", cellType = ColumnType.NUMERIC)
    private Long paymentId;

    /** 支付单号 */
    @Excel(name = "支付单号")
    private String paymentNo;

    /** 订单ID */
    private Long orderId;

    /** 支付用户ID */
    private Long userId;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal amount;

    /** 支付状态 */
    @Excel(name = "支付状态")
    private String status;

    /** 模拟交易流水号 */
    private String transactionNo;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date paidTime;

    /** 乐观锁版本 */
    private Integer version;

    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
    }

    @NotBlank(message = "支付单号不能为空")
    @Size(min = 0, max = 64, message = "支付单号长度不能超过64个字符")
    public String getPaymentNo()
    {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo)
    {
        this.paymentNo = paymentNo;
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

    @NotNull(message = "支付用户ID不能为空")
    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    @NotNull(message = "支付金额不能为空")
    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Size(min = 0, max = 64, message = "交易流水号长度不能超过64个字符")
    public String getTransactionNo()
    {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo)
    {
        this.transactionNo = transactionNo;
    }

    public Date getPaidTime()
    {
        return paidTime;
    }

    public void setPaidTime(Date paidTime)
    {
        this.paidTime = paidTime;
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
            .append("paymentId", getPaymentId())
            .append("paymentNo", getPaymentNo())
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("amount", getAmount())
            .append("status", getStatus())
            .append("transactionNo", getTransactionNo())
            .append("paidTime", getPaidTime())
            .append("version", getVersion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
