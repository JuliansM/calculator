package com.tenpo.calculator.service;

import com.tenpo.calculator.exception.BusinessException;
import com.tenpo.calculator.exception.TechnicalException;
import com.tenpo.calculator.model.CallHistory;
import com.tenpo.calculator.model.record.CalculationRequest;
import com.tenpo.calculator.model.record.CalculationResponse;
import com.tenpo.calculator.repository.CallHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CalculationServiceTest {

    @Mock
    private PercentageExternalService percentageExternalService;

    @Mock
    private CallHistoryRepository callHistoryRepository;

    @InjectMocks
    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getCallHistory_success() {
        CallHistory callHistory = CallHistory.builder()
                .id(1L)
                .timestamp(LocalDateTime.now())
                .endpoint("/calculate")
                .parameters("params")
                .response("response")
                .error(null)
                .build();

        when(callHistoryRepository.findAll()).thenReturn(Flux.just(callHistory));

        StepVerifier.create(calculationService.getCallHistory())
                .expectNextMatches(response -> {
                    List<?> histories = (List<?>) response.getData();
                    return response.getMessage() != null && histories.size() == 1;
                })
                .verifyComplete();

        verify(callHistoryRepository).findAll();
    }

    @Test
    void getCallHistory_error() {
        when(callHistoryRepository.findAll()).thenReturn(Flux.error(new RuntimeException("DB error")));

        StepVerifier.create(calculationService.getCallHistory())
                .expectError(TechnicalException.class)
                .verify();

        verify(callHistoryRepository).findAll();
    }

    @Test
    void calculateSumAndPercentage_success() {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("10"),
                new BigDecimal("20")
        );

        when(percentageExternalService.getPercentage()).thenReturn(Mono.just(new BigDecimal("0.1")));
        when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(Mono.just(CallHistory.builder().build()));

        StepVerifier.create(calculationService.calculateSumAndPorcentage(request))
                .expectNextMatches(response -> {
                    CalculationResponse calculation = (CalculationResponse) response.getData();
                    return calculation.sum().equals(new BigDecimal("30"))
                            && calculation.calculatedPercentage().equals(new BigDecimal("33.0"));
                })
                .verifyComplete();

        verify(percentageExternalService).getPercentage();
        verify(callHistoryRepository, atLeastOnce()).save(any(CallHistory.class));
    }

    @Test
    void calculateSumAndPercentage_error() {
        CalculationRequest request = new CalculationRequest(
                new BigDecimal("5"),
                new BigDecimal("15")
        );

        when(percentageExternalService.getPercentage()).thenReturn(Mono.error(new RuntimeException("External service down")));
        when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(Mono.just(CallHistory.builder().build()));

        StepVerifier.create(calculationService.calculateSumAndPorcentage(request))
                .expectError(BusinessException.class)
                .verify();

        verify(percentageExternalService).getPercentage();
        verify(callHistoryRepository, atLeastOnce()).save(any(CallHistory.class));
    }
}
