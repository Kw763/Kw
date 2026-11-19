package com.ruoyi.takeout.domain;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外卖商品对象 takeout_product
 *
 * @author ruoyi
 */
public class TakeoutProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Excel(name = "商品ID", cellType = ColumnType.NUMERIC)
    private Long productId;

    /** 商家ID */
    @Excel(name = "商家ID", cellType = ColumnType.NUMERIC)
    private Long merchantId;

    /** 商家名称（关联查询） */
    @Excel(name = "商家名称")
    private String merchantName;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 分类名称 */
    @Excel(name = "分类")
    private String categoryName;

    /** 商品价格 */
    @Excel(name = "商品价格")
    private BigDecimal price;

    /** 商品图片 */
    private String image;

    /** 商品描述 */
    private String description;

    /** 上架状态（0上架 1下架） */
    @Excel(name = "上架状态", readConverterExp = "0=上架,1=下架")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 乐观锁版本 */
    private Integer version;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
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

    @NotBlank(message = "商品名称不能为空")
    @Size(min = 0, max = 100, message = "商品名称长度不能超过100个字符")
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @Size(min = 0, max = 50, message = "分类名称长度不能超过50个字符")
    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    @NotNull(message = "商品价格不能为空")
    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    @Size(min = 0, max = 255, message = "商品图片长度不能超过255个字符")
    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    @Size(min = 0, max = 500, message = "商品描述长度不能超过500个字符")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
            .append("productId", getProductId())
            .append("merchantId", getMerchantId())
            .append("merchantName", getMerchantName())
            .append("productName", getProductName())
            .append("categoryName", getCategoryName())
            .append("price", getPrice())
            .append("image", getImage())
            .append("description", getDescription())
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
