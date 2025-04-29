package com.tenpo.calculator.controller;

import com.tenpo.calculator.model.record.CalculationRequest;
import com.tenpo.calculator.model.dto.StandardResponse;
import com.tenpo.calculator.service.CalculationService;
import com.tenpo.calculator.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = Constants.SWAGGER_CONTROLLER_TAG_NAME, description = Constants.SWAGGER_CONTROLLER_TAG_DESCRIPTION)
public class CalculationController {

    private final CalculationService calculationService;

    @Operation(
            summary = Constants.SWAGGER_CONTROLLER_OPERATION_CALCULATE_SUMMARY,
            description = Constants.SWAGGER_CONTROLLER_OPERATION_CALCULATE_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constants.SWAGGER_CONTROLLER_API_RESPONSE_200,
                    description = Constants.SWAGGER_CONTROLLER_API_RESPONSE_CALCULATE_200_DESCRIPTION),
            @ApiResponse(responseCode = Constants.SWAGGER_CONTROLLER_API_RESPONSE_400,
                    description = Constants.SWAGGER_CONTROLLER_API_RESPONSE_400_DESCRIPTION),
            @ApiResponse(responseCode = Constants.SWAGGER_CONTROLLER_API_RESPONSE_500,
                    description = Constants.SWAGGER_CONTROLLER_API_RESPONSE_500_DESCRIPTION)
    })
    @PostMapping(Constants.CALCULATE_PATH)
    public Mono<ResponseEntity<StandardResponse>> calculate(@RequestBody CalculationRequest request) {
        return calculationService.calculateSumAndPorcentage(request)
                .map(ResponseEntity::ok);
    }

    @Operation(
            summary = Constants.SWAGGER_CONTROLLER_OPERATION_GET_HISTORY_SUMMARY,
            description = Constants.SWAGGER_CONTROLLER_OPERATION_GET_HISTORY_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constants.SWAGGER_CONTROLLER_API_RESPONSE_200,
                    description = Constants.SWAGGER_CONTROLLER_API_RESPONSE_GET_HISTORY_200_DESCRIPTION),
            @ApiResponse(responseCode = Constants.SWAGGER_CONTROLLER_API_RESPONSE_500,
                    description = Constants.SWAGGER_CONTROLLER_API_RESPONSE_500_DESCRIPTION)
    })
    @GetMapping(Constants.GET_CALL_HISTORY_PATH)
    public Mono<ResponseEntity<StandardResponse>> getHistory() {
        return calculationService.getCallHistory()
                .map(ResponseEntity::ok);
    }
}