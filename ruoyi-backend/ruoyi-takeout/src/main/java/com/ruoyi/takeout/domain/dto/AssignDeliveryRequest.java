package com.ruoyi.takeout.domain.dto;

import jakarta.validation.constraints.NotNull;

/** 人工派单请求。 */
public record AssignDeliveryRequest(@NotNull Long riderId)
{
}
