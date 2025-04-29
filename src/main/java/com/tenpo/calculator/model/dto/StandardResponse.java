package com.tenpo.calculator.model.dto;

import com.tenpo.calculator.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = Constants.SWAGGER_STANDARD_RESPONSE_TITLE)
public class StandardResponse {

    private String message;
    private Object data;
}
