package com.tenpo.calculator.mapper;

import com.tenpo.calculator.model.CallHistory;
import com.tenpo.calculator.model.dto.CallHistoryDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CallHistoryMapper {

    public static CallHistoryDTO toDTO(CallHistory callHistory) {
        return Optional.ofNullable(callHistory)
                .map(callHistory_ -> CallHistoryDTO.builder()
                        .id(callHistory.getId())
                        .timestamp(callHistory.getTimestamp())
                        .endpoint(callHistory.getEndpoint())
                        .parameters(callHistory.getParameters())
                        .response(callHistory.getResponse())
                        .error(callHistory.getError())
                        .build())
                .orElse(null);
    }
}
