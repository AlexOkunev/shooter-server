package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    public static final String SINGLE_THREAD_EQUIPMENT_KAFKA_LISTENER_CONTAINER_FACTORY = "singleThreadEquipmentKafkaListenerContainerFactory";

    public static final String SINGLE_THREAD_PLAYERS_KAFKA_LISTENER_CONTAINER_FACTORY = "singleThreadPlayersKafkaListenerContainerFactory";

    @Bean(name = SINGLE_THREAD_EQUIPMENT_KAFKA_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<Object, Object> singleThreadEquipmentKafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory,
            @Qualifier(KafkaErrorHandlerConfig.KAFKA_ERROR_HANDLER) DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean(name = SINGLE_THREAD_PLAYERS_KAFKA_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<Object, Object> singleThreadPlayersKafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory,
            @Qualifier(KafkaErrorHandlerConfig.KAFKA_ERROR_HANDLER) DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
