package com.tenpo.calculator.model.record;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record CalculationResponse(BigDecimal calculatedPercentage, BigDecimal sum) {
}
