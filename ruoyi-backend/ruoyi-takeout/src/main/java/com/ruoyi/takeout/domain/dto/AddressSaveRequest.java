package com.ruoyi.takeout.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** 地址新增和修改请求。 */
public record AddressSaveRequest(
        @NotBlank @Size(max = 50) String receiverName,
        @NotBlank @Size(max = 20) String receiverPhone,
        @NotBlank @Size(max = 50) String province,
        @NotBlank @Size(max = 50) String city,
        @NotBlank @Size(max = 50) String district,
        @NotBlank @Size(max = 255) String detailAddress,
        @Pattern(regexp = "^[YN]$", message = "默认地址标识只能是Y或N") String isDefault,
        @Size(max = 500) String remark)
{
}
