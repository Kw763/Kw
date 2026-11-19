package com.ruoyi.takeout.domain.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** 创建订单请求，金额和商品价格不接受客户端传入。 */
public record CreateOrderRequest(
        @NotBlank @Size(max = 64) String requestNo,
        @NotNull Long addressId,
        @NotNull Long merchantId,
        @Size(max = 500) String remark,
        @NotEmpty List<@Valid CreateOrderItem> items)
{
    public record CreateOrderItem(@NotNull Long productId,
            @NotNull @Min(1) @Max(999) Integer quantity) { }
}
