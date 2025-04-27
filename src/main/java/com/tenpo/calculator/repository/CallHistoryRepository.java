package com.tenpo.calculator.repository;

import com.tenpo.calculator.model.CallHistory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallHistoryRepository extends ReactiveCrudRepository<CallHistory, Long> {
}
