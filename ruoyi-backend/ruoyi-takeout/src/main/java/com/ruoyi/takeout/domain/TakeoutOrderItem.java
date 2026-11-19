package com.ruoyi.takeout.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;

/**
 * 订单商品快照对象 takeout_order_item
 *
 * @author ruoyi
 */
public class TakeoutOrderItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    @Excel(name = "明细ID", cellType = ColumnType.NUMERIC)
    private Long itemId;

    /** 订单ID */
    private Long orderId;

    /** 商品ID */
    @Excel(name = "商品ID", cellType = ColumnType.NUMERIC)
    private Long productId;

    /** 商品名称快照 */
    @Excel(name = "商品名称")
    private String productName;

    /** 商品图片快照 */
    private String productImage;

    /** 商品单价快照 */
    @Excel(name = "单价")
    private BigDecimal productPrice;

    /** 数量 */
    @Excel(name = "数量")
    private Integer quantity;

    /** 小计 */
    @Excel(name = "小计")
    private BigDecimal subtotal;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    @NotNull(message = "商品ID不能为空")
    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    @Size(min = 0, max = 100, message = "商品名称长度不能超过100个字符")
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @Size(min = 0, max = 255, message = "商品图片长度不能超过255个字符")
    public String getProductImage()
    {
        return productImage;
    }

    public void setProductImage(String productImage)
    {
        this.productImage = productImage;
    }

    @NotNull(message = "商品单价不能为空")
    public BigDecimal getProductPrice()
    {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice)
    {
        this.productPrice = productPrice;
    }

    @NotNull(message = "数量不能为空")
    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    @NotNull(message = "小计不能为空")
    public BigDecimal getSubtotal()
    {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal)
    {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("orderId", getOrderId())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("productImage", getProductImage())
            .append("productPrice", getProductPrice())
            .append("quantity", getQuantity())
            .append("subtotal", getSubtotal())
            .toString();
    }
}
