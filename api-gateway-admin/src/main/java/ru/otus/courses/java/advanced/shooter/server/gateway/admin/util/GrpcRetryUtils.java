package ru.otus.courses.java.advanced.shooter.server.gateway.admin.util;

import io.grpc.MethodDescriptor;
import lombok.experimental.UtilityClass;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.GrpcRetryProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class GrpcRetryUtils {

    public static Map<String, Object> buildServiceConfig(
            GrpcRetryProperties retryProps,
            List<MethodDescriptor<?, ?>> methodDescriptors
    ) {
        Map<String, Object> config = new HashMap<>();

        List<Map<String, Object>> names = methodDescriptors.stream()
                .filter(md -> md.getServiceName() != null && md.getBareMethodName() != null)
                .map(md -> Map.<String, Object>ofEntries(
                        Map.entry("service", md.getServiceName()),
                        Map.entry("method", md.getBareMethodName())))
                .toList();

        Map<String, Object> methodConfig = Map.of(
                "name", names,
                "retryPolicy", Map.of(
                        "maxAttempts", (double) retryProps.maxAttempts(),
                        "initialBackoff", retryProps.initialBackoff(),
                        "maxBackoff", retryProps.maxBackoff(),
                        "backoffMultiplier", retryProps.backoffMultiplier(),
                        "retryableStatusCodes", retryProps.retryableStatusCodes()
                )
        );

        config.put("methodConfig", List.of(methodConfig));

        if (retryProps.throttling() != null && retryProps.throttling().enabled()) {
            config.put("retryThrottling", Map.of(
                    "maxTokens", retryProps.throttling().maxTokens(),
                    "tokenRatio", retryProps.throttling().tokenRatio()
            ));
        }

        System.out.println("Generated config: " + config);

        return config;
    }
}
