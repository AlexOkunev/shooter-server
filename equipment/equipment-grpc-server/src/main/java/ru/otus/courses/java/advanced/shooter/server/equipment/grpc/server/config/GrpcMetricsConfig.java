package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.config;

import io.grpc.ServerInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.grpc.MetricCollectingServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcMetricsConfig {

    @Bean
    @GRpcGlobalInterceptor
    public ServerInterceptor grpcMetricsInterceptor(MeterRegistry registry) {
        return new MetricCollectingServerInterceptor(registry);
    }
}