package ru.jb.db_spring.api.dto;

import java.math.BigDecimal;
import java.util.Map;

public record BalanceSummaryDto(Long id,
                                BigDecimal totalAmount,
                                Map<String, BigDecimal> byType) {
}
