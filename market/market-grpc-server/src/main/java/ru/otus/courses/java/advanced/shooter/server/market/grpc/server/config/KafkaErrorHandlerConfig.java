package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaErrorHandlerConfig {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(1000L, 3L));

        errorHandler.setRetryListeners((record, ex, attemptNumber) ->
                log.warn("Retry attempt {} for topic {} record key {} value {}. Cause: {}",
                        attemptNumber, record.topic(), record.key(), record.value(), ex.getMessage()));

        return errorHandler;
    }
}
