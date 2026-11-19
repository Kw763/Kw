package com.ruoyi.takeout.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** 商品上下架请求。 */
public record ProductStatusRequest(
        @NotBlank @Pattern(regexp = "^[01]$", message = "商品状态只能是0或1") String status)
{
}
