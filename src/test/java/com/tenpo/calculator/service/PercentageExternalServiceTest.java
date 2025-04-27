package com.tenpo.calculator.service;

import com.tenpo.calculator.exception.BusinessException;
import com.tenpo.calculator.model.record.PercentageResponse;
import com.tenpo.calculator.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PercentageExternalServiceTest {

    @Mock
    private ReactiveStringRedisTemplate reactiveRedisTemplate;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private ReactiveValueOperations<String, String> valueOperations;

    @InjectMocks
    private PercentageExternalService percentageExternalService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getPercentage_success_fetchFromExternal() {
        BigDecimal expectedPercentage = BigDecimal.valueOf(0.75);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PercentageResponse.class))
                .thenReturn(Mono.just(new PercentageResponse(expectedPercentage)));

        when(reactiveRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.set(anyString(), anyString(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(percentageExternalService.getPercentage())
                .expectNext(expectedPercentage)
                .verifyComplete();

        verify(valueOperations).set(Constants.CACHE_PERCENTAGE_KEY, expectedPercentage.toString(), Duration.ofMinutes(Constants.CACHE_DURATION_MINUTES));
    }

    @Test
    void getPercentage_success_fetchFromCacheWhenExternalFails() {
        BigDecimal cachedPercentage = BigDecimal.valueOf(0.50);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PercentageResponse.class))
                .thenReturn(Mono.error(new RuntimeException("Service unavailable")));

        when(reactiveRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(Constants.CACHE_PERCENTAGE_KEY)).thenReturn(Mono.just(cachedPercentage.toString()));

        StepVerifier.create(percentageExternalService.getPercentage())
                .expectNext(cachedPercentage)
                .verifyComplete();
    }

    @Test
    void getPercentage_failure_externalAndCacheEmpty() {
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PercentageResponse.class))
                .thenReturn(Mono.error(new RuntimeException("External down")));

        when(reactiveRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(Constants.CACHE_PERCENTAGE_KEY)).thenReturn(Mono.empty());

        StepVerifier.create(percentageExternalService.getPercentage())
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void fetchPercentageFromExternalService_errorWrapsTechnicalException() {
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PercentageResponse.class))
                .thenReturn(Mono.error(WebClientResponseException.create(500, "Error", null, null, null)));

        when(reactiveRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(Constants.CACHE_PERCENTAGE_KEY)).thenReturn(Mono.empty());

        Mono<BigDecimal> result = percentageExternalService.getPercentage();

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof BusinessException)
                .verify();
    }
}
