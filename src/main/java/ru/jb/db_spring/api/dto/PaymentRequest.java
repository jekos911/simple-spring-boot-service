package ru.jb.db_spring.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest (
        @NotNull Long userId,
        @NotNull Long productId,
        @NotNull @Min(1) BigDecimal amount){
}
