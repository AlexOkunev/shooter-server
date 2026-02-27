package ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties;

import java.util.List;

public record GrpcRetryProperties(
        boolean enabled,
        int maxAttempts,
        String initialBackoff,
        String maxBackoff,
        double backoffMultiplier,
        List<String> retryableStatusCodes,
        RetryThrottlingProperties throttling
) {
    public record RetryThrottlingProperties(
            boolean enabled,
            double maxTokens,
            double tokenRatio
    ) {

    }
}
