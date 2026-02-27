package ru.otus.courses.java.advanced.shooter.server.gateway.admin.config;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcCircuitBreakerCustomizers {

    private static boolean recordGrpcFailureDefault(Throwable ex) {
        if (!(ex instanceof StatusRuntimeException sre)) {
            return true;
        }

        Status.Code code = sre.getStatus().getCode();

        return switch (code) {
            case UNAVAILABLE, DEADLINE_EXCEEDED, INTERNAL, UNKNOWN, RESOURCE_EXHAUSTED -> true;
            default -> false;
        };
    }

    @Bean
    public CircuitBreakerConfigCustomizer grpcEquipmentCbCustomizer() {
        return CircuitBreakerConfigCustomizer.of(
                "grpc_equipment_cb",
                builder -> builder.recordException(GrpcCircuitBreakerCustomizers::recordGrpcFailureDefault)
        );
    }

    @Bean
    public CircuitBreakerConfigCustomizer grpcInventoryCbCustomizer() {
        return CircuitBreakerConfigCustomizer.of(
                "grpc_inventory_cb",
                builder -> builder.recordException(GrpcCircuitBreakerCustomizers::recordGrpcFailureDefault)
        );
    }

    @Bean
    public CircuitBreakerConfigCustomizer grpcMarketCbCustomizer() {
        return CircuitBreakerConfigCustomizer.of(
                "grpc_market_cb",
                builder -> builder.recordException(GrpcCircuitBreakerCustomizers::recordGrpcFailureDefault)
        );
    }

    @Bean
    public CircuitBreakerConfigCustomizer grpcPlayerCbCustomizer() {
        return CircuitBreakerConfigCustomizer.of(
                "grpc_player_cb",
                builder -> builder.recordException(GrpcCircuitBreakerCustomizers::recordGrpcFailureDefault)
        );
    }
}