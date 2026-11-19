package com.ruoyi.takeout.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户收货地址对象 takeout_address
 *
 * @author ruoyi
 */
public class TakeoutAddress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地址ID */
    @Excel(name = "地址ID", cellType = ColumnType.NUMERIC)
    private Long addressId;

    /** 所属用户ID */
    private Long userId;

    /** 所属用户名称（关联查询） */
    private String userName;

    /** 收货人 */
    @Excel(name = "收货人")
    private String receiverName;

    /** 收货电话 */
    @Excel(name = "收货电话")
    private String receiverPhone;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区县 */
    @Excel(name = "区县")
    private String district;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String detailAddress;

    /** 是否默认（Y是 N否） */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    private String isDefault;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getAddressId()
    {
        return addressId;
    }

    public void setAddressId(Long addressId)
    {
        this.addressId = addressId;
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

    @NotBlank(message = "省不能为空")
    @Size(min = 0, max = 50, message = "省长度不能超过50个字符")
    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    @NotBlank(message = "市不能为空")
    @Size(min = 0, max = 50, message = "市长度不能超过50个字符")
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @NotBlank(message = "区县不能为空")
    @Size(min = 0, max = 50, message = "区县长度不能超过50个字符")
    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    @NotBlank(message = "详细地址不能为空")
    @Size(min = 0, max = 255, message = "详细地址长度不能超过255个字符")
    public String getDetailAddress()
    {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress)
    {
        this.detailAddress = detailAddress;
    }

    public String getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("addressId", getAddressId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("receiverName", getReceiverName())
            .append("receiverPhone", getReceiverPhone())
            .append("province", getProvince())
            .append("city", getCity())
            .append("district", getDistrict())
            .append("detailAddress", getDetailAddress())
            .append("isDefault", getIsDefault())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
