package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.config;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.exception.AbsentPaymentException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.exception.InvalidMoneyBundleTradeStatusException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaErrorHandlerConfig {

    public static final String KAFKA_ERROR_HANDLER = "kafkaErrorHandler";

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Bean(name = KAFKA_ERROR_HANDLER)
    public DefaultErrorHandler kafkaErrorHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition())
                ),
                new FixedBackOff(1000L, 3L)
        );

        errorHandler.addNotRetryableExceptions(ValidationException.class, IllegalArgumentException.class,
                AbsentPaymentException.class, InvalidMoneyBundleTradeStatusException.class);

        errorHandler.setRetryListeners((record, ex, attempt) ->
                log.error("Retry {} topic={} partition={} offset={} key={} ex={} msg={}",
                        attempt, record.topic(), record.partition(), record.offset(), record.key(),
                        ex.getClass().getSimpleName(), ex.getMessage(), ex)
        );

        return errorHandler;
    }
}
