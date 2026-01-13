package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.config.KafkaListenerConfig;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.players.kafka.types.Player;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerMessageListener {

    private final PlayerInventoryService playerInventoryService;

    @KafkaListener(
            topics = "${players-kafka.topic}",
            groupId = "${players-kafka.group-id}",
            containerFactory = KafkaListenerConfig.SINGLE_THREAD_PLAYERS_KAFKA_LISTENER_CONTAINER_FACTORY
    )
    public void handle(Player message, @Header(name = "__op", required = false) String op) {
        log.info("Player received: uuid={}, enabled={}, name={}. Operation: {}",
                message.getPlayerUuid(), message.getEnabled(), message.getLogin(), op);

        switch (op) {
            case "c", "r" ->
                    playerInventoryService.initializePlayerInventoryBySystemEvent(UUID.fromString(message.getPlayerUuid()));
            default -> log.warn("Unprocessed operation {} for player with uuid: {}", op, message.getPlayerUuid());
        }

        log.info("Player inventory initialized for player with uuid: {}", message.getPlayerUuid());
    }
}
