package com.ruoyi.takeout.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外卖商家对象 takeout_merchant
 *
 * @author ruoyi
 */
public class TakeoutMerchant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商家ID */
    @Excel(name = "商家ID", cellType = ColumnType.NUMERIC)
    private Long merchantId;

    /** 商家名称 */
    @Excel(name = "商家名称")
    private String merchantName;

    /** 商家所属用户ID */
    private Long ownerUserId;

    /** 商家所属用户名称（关联查询） */
    private String ownerName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** Logo */
    private String logo;

    /** 商家地址 */
    @Excel(name = "商家地址")
    private String address;

    /** 营业开始时间HH:mm */
    @Excel(name = "营业开始")
    private String openTime;

    /** 营业结束时间HH:mm */
    @Excel(name = "营业结束")
    private String closeTime;

    /** 商家公告 */
    private String notice;

    /** 营业状态（0营业 1休息） */
    @Excel(name = "营业状态", readConverterExp = "0=营业,1=休息")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 乐观锁版本 */
    private Integer version;

    public Long getMerchantId()
    {
        return merchantId;
    }

    public void setMerchantId(Long merchantId)
    {
        this.merchantId = merchantId;
    }

    @NotBlank(message = "商家名称不能为空")
    @Size(min = 0, max = 100, message = "商家名称长度不能超过100个字符")
    public String getMerchantName()
    {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    public Long getOwnerUserId()
    {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    @Size(min = 0, max = 20, message = "联系电话长度不能超过20个字符")
    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    @Size(min = 0, max = 255, message = "Logo长度不能超过255个字符")
    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    @NotBlank(message = "商家地址不能为空")
    @Size(min = 0, max = 255, message = "商家地址长度不能超过255个字符")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @NotBlank(message = "营业开始时间不能为空")
    @Size(min = 0, max = 5, message = "营业开始时间长度不能超过5个字符")
    public String getOpenTime()
    {
        return openTime;
    }

    public void setOpenTime(String openTime)
    {
        this.openTime = openTime;
    }

    @NotBlank(message = "营业结束时间不能为空")
    @Size(min = 0, max = 5, message = "营业结束时间长度不能超过5个字符")
    public String getCloseTime()
    {
        return closeTime;
    }

    public void setCloseTime(String closeTime)
    {
        this.closeTime = closeTime;
    }

    @Size(min = 0, max = 500, message = "商家公告长度不能超过500个字符")
    public String getNotice()
    {
        return notice;
    }

    public void setNotice(String notice)
    {
        this.notice = notice;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
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
            .append("merchantId", getMerchantId())
            .append("merchantName", getMerchantName())
            .append("ownerUserId", getOwnerUserId())
            .append("ownerName", getOwnerName())
            .append("contactPhone", getContactPhone())
            .append("logo", getLogo())
            .append("address", getAddress())
            .append("openTime", getOpenTime())
            .append("closeTime", getCloseTime())
            .append("notice", getNotice())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("version", getVersion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
