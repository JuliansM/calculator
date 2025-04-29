package com.tenpo.calculator.service;

import com.tenpo.calculator.exception.BusinessException;
import com.tenpo.calculator.exception.TechnicalException;
import com.tenpo.calculator.model.record.PercentageResponse;
import com.tenpo.calculator.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class PercentageExternalService {

    private final ReactiveStringRedisTemplate reactiveRedisTemplate;
    private final WebClient webClient;

    public Mono<BigDecimal> getPercentage() {

        return fetchPercentageFromExternalService()
                .flatMap(this::cachePercentage)
                .onErrorResume(TechnicalException.class::isInstance,
                        throwable -> getPercentageFromCache())
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new BusinessException(Constants.ERROR_GETTING_PERCENTAGE_EXTERNAL_SERVICE_AND_CACHE))));
    }

    private Mono<BigDecimal> fetchPercentageFromExternalService() {
        return webClient
                .get()
                .retrieve()
                .bodyToMono(PercentageResponse.class)
                .map(PercentageResponse::percentage)
                .doOnSubscribe(subscription -> log.info(Constants.LOG_GETTING_PERCENTAGE_EXTERNAL_SERVICE_SUBSCRIBE))
                .doOnSuccess(percentage -> log.info(Constants.LOG_GETTING_PERCENTAGE_EXTERNAL_SERVICE_SUCCESS, percentage))
                .doOnError(throwable -> log.error(Constants.LOG_GETTING_PERCENTAGE_EXTERNAL_SERVICE_ERROR, throwable.getMessage()))
                .onErrorResume(throwable -> Mono.error(new TechnicalException(Constants.ERROR_GETTING_PERCENTAGE_EXTERNAL_SERVICE_ERROR, throwable)));
    }

    private Mono<BigDecimal> cachePercentage(BigDecimal percentage) {
        return reactiveRedisTemplate.opsForValue()
                .set(Constants.CACHE_PERCENTAGE_KEY, percentage.toString(), Duration.ofMinutes(Constants.CACHE_DURATION_MINUTES))
                .thenReturn(percentage)
                .doOnSuccess(percentage1 -> log.info(Constants.LOG_NEW_PERCENTAGE_SAVED_CACHE, percentage1));
    }

    private Mono<BigDecimal> getPercentageFromCache() {
        return reactiveRedisTemplate.opsForValue()
                .get(Constants.CACHE_PERCENTAGE_KEY)
                .doOnSuccess(percentage -> log.info(Constants.LOG_GETTING_PERCENTAGE_CACHE_SUCCESS, percentage))
                .map(BigDecimal::new);
    }
}
