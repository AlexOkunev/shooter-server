package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class RateLimitingGrpcCallExecutor {

    public <T> T execute(Supplier<T> call, List<RateLimiter> limiters) {
        Objects.requireNonNull(limiters, "limiters");
        Objects.requireNonNull(call, "call");

        Supplier<T> decorated = decorate(limiters, call);

        return decorated.get();
    }

    public final <T> T execute(Supplier<T> call, RateLimiter... limiters) {
        Objects.requireNonNull(limiters, "limiters");
        return execute(call, List.of(limiters));
    }

    private <T> Supplier<T> decorate(List<RateLimiter> rateLimiters, Supplier<T> call) {
        Supplier<T> decorated = call;

        for (RateLimiter rateLimiter : rateLimiters.reversed()) {
            Objects.requireNonNull(rateLimiter, "rateLimiter");
            decorated = RateLimiter.decorateSupplier(rateLimiter, decorated);
        }

        return decorated;
    }
}
