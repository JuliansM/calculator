package com.tenpo.calculator.model.record;

import com.tenpo.calculator.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.math.BigDecimal;

@Schema(description = Constants.SWAGGER_REQUEST_TITLE)
public record CalculationRequest(
        @Schema(description = Constants.SWAGGER_REQUEST_NUMBER1, example = Constants.SWAGGER_REQUEST_NUMBER1_EXAMPLE)
        @NonNull BigDecimal num1,
        @Schema(description = Constants.SWAGGER_REQUEST_NUMBER2, example = Constants.SWAGGER_REQUEST_NUMBER2_EXAMPLE)
        @NonNull BigDecimal num2
) {
}
