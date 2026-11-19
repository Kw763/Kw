package com.ruoyi.takeout.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外卖业务状态日志对象 takeout_status_log
 *
 * @author ruoyi
 */
public class TakeoutStatusLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    @Excel(name = "日志ID", cellType = ColumnType.NUMERIC)
    private Long logId;

    /** 业务类型（ORDER/PAYMENT/DELIVERY） */
    @Excel(name = "业务类型")
    private String bizType;

    /** 业务ID */
    @Excel(name = "业务ID", cellType = ColumnType.NUMERIC)
    private Long bizId;

    /** 原状态，创建时为空字符串 */
    @Excel(name = "原状态")
    private String fromStatus;

    /** 目标状态 */
    @Excel(name = "目标状态")
    private String toStatus;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人名称 */
    @Excel(name = "操作人")
    private String operatorName;

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    @NotBlank(message = "业务类型不能为空")
    @Size(min = 0, max = 20, message = "业务类型长度不能超过20个字符")
    public String getBizType()
    {
        return bizType;
    }

    public void setBizType(String bizType)
    {
        this.bizType = bizType;
    }

    @NotNull(message = "业务ID不能为空")
    public Long getBizId()
    {
        return bizId;
    }

    public void setBizId(Long bizId)
    {
        this.bizId = bizId;
    }

    @Size(min = 0, max = 20, message = "原状态长度不能超过20个字符")
    public String getFromStatus()
    {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus)
    {
        this.fromStatus = fromStatus;
    }

    @NotBlank(message = "目标状态不能为空")
    @Size(min = 0, max = 20, message = "目标状态长度不能超过20个字符")
    public String getToStatus()
    {
        return toStatus;
    }

    public void setToStatus(String toStatus)
    {
        this.toStatus = toStatus;
    }

    public Long getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(Long operatorId)
    {
        this.operatorId = operatorId;
    }

    @Size(min = 0, max = 64, message = "操作人名称长度不能超过64个字符")
    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("bizType", getBizType())
            .append("bizId", getBizId())
            .append("fromStatus", getFromStatus())
            .append("toStatus", getToStatus())
            .append("operatorId", getOperatorId())
            .append("operatorName", getOperatorName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
