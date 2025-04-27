package com.tenpo.calculator.controller;

import com.tenpo.calculator.model.record.CalculationRequest;
import com.tenpo.calculator.model.dto.StandardResponse;
import com.tenpo.calculator.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculationService calculationService;

    @PostMapping("/calculate")
    public Mono<ResponseEntity<StandardResponse>> calculate(@RequestBody CalculationRequest request) {
        return calculationService.calculateSumAndPorcentage(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/get-call-history")
    public Mono<ResponseEntity<StandardResponse>> getHistory() {
        return calculationService.getCallHistory()
                .map(ResponseEntity::ok);
    }

}
