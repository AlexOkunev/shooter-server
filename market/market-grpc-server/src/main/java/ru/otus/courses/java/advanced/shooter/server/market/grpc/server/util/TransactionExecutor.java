package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionExecutor {
    private final TransactionTemplate transactionTemplate;

    public <T> T execute(Supplier<T> action) {
        return transactionTemplate.execute(status -> action.get());
    }

    public void execute(Runnable action) {
        transactionTemplate.executeWithoutResult(status -> action.run());
    }
}
