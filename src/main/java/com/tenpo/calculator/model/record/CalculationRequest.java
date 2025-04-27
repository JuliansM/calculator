package com.tenpo.calculator.model.record;

import lombok.NonNull;

import java.math.BigDecimal;

public record CalculationRequest(@NonNull BigDecimal num1, @NonNull BigDecimal num2) {
}
