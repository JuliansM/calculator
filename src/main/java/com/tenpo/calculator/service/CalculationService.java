package com.tenpo.calculator.service;

import com.tenpo.calculator.exception.BusinessException;
import com.tenpo.calculator.exception.TechnicalException;
import com.tenpo.calculator.mapper.CallHistoryMapper;
import com.tenpo.calculator.model.record.CalculationRequest;
import com.tenpo.calculator.model.record.CalculationResponse;
import com.tenpo.calculator.model.CallHistory;
import com.tenpo.calculator.model.dto.StandardResponse;
import com.tenpo.calculator.repository.CallHistoryRepository;
import com.tenpo.calculator.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {

    private final PercentageExternalService percentageExternalService;
    private final CallHistoryRepository callHistoryRepository;

    public Mono<StandardResponse> getCallHistory() {

        return callHistoryRepository.findAll()
                .map(CallHistoryMapper::toDTO)
                .collectList()
                .flatMap(callHistoryDTOS -> Mono.just(StandardResponse.builder()
                        .message(Constants.GET_HISTORY_SUCCESS_MESSAGE)
                        .data(callHistoryDTOS)
                        .build()))
                .onErrorResume(throwable -> Mono.error(new TechnicalException(Constants.GET_HISTORY_ERROR_MESSAGE)))
                .doOnError(throwable -> log.error(Constants.LOG_GETTING_HISTORY_ERROR, throwable.getMessage()));
    }

    public Mono<StandardResponse> calculateSumAndPorcentage(CalculationRequest calculationRequest) {

        return percentageExternalService.getPercentage()
                .flatMap(percentage -> {
                    BigDecimal sum = calculationRequest.num1().add(calculationRequest.num2());
                    BigDecimal calculatedPercentage = sum.add(sum.multiply(percentage));

                    CalculationResponse calculationResponse = CalculationResponse.builder()
                            .sum(sum)
                            .calculatedPercentage(calculatedPercentage)
                            .build();

                    saveSuccessCallHistory(calculationRequest.toString(), calculationResponse.toString())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();

                    return Mono.just(StandardResponse.builder()
                            .message(Constants.CALC_OPERATIONS_SUCCESS_MESSAGE)
                            .data(calculationResponse)
                            .build());
                })
                .onErrorResume(exception -> {
                    saveCallHistoryWithError(calculationRequest.toString(), exception.getMessage())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();

                    return Mono.error(new BusinessException(Constants.ERROR_GETTING_PERCENTAGE));
                })
                .doOnSubscribe(subscription -> log.info(Constants.LOG_CALCULATION_REQUEST, calculationRequest))
                .doOnSuccess(response -> log.info(Constants.LOG_CALCULATION_RESPONSE, response))
                .doOnError(exception -> log.error(Constants.LOG_CALCULATION_ERROR, exception.getMessage()));
    }

    private Mono<Void> saveSuccessCallHistory(String parameters, String response) {

        return saveCallHistory(CallHistory.builder()
                .timestamp(LocalDateTime.now())
                .endpoint(Constants.CALCULATE_PATH)
                .parameters(parameters)
                .response(response)
                .build());
    }

    private Mono<Void> saveCallHistoryWithError(String parameters, String error) {

        return saveCallHistory(CallHistory.builder()
                .timestamp(LocalDateTime.now())
                .endpoint(Constants.CALCULATE_PATH)
                .parameters(parameters)
                .error(error)
                .build());
    }

    private Mono<Void> saveCallHistory(CallHistory callHistory) {

        return callHistoryRepository.save(callHistory)
                .doOnSuccess(callHistory1 -> log.info(Constants.LOG_SAVE_HISTORY_SUCCESS, callHistory1))
                .onErrorResume(throwable -> {
                    log.error(Constants.LOG_SAVE_HISTORY_ERROR, throwable.getMessage());
                    return Mono.empty();
                }).then();
    }
}
