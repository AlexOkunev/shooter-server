package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.config.KafkaListenerConfig;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.players.kafka.types.Player;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerMessageListener {

    private final PlayerAccountService playerAccountService;

    @KafkaListener(
            topics = "${players-kafka.topic}",
            groupId = "${players-kafka.group-id}",
            containerFactory = KafkaListenerConfig.SINGLE_THREAD_PLAYERS_KAFKA_LISTENER_CONTAINER_FACTORY
    )
    public void handle(Player message, @Header(name = "__op", required = false) String op) {
        log.info("Player received: uuid={}, enabled={}, name={}. Operation: {}",
                message.getPlayerUuid(), message.getEnabled(), message.getLogin(), op);

        switch (op) {
            case "c", "r" -> {
                log.info("Process operation {} for player with uuid: {}", op, message.getPlayerUuid());

                playerAccountService.initializePlayerAccountBySystemEvent(UUID.fromString(message.getPlayerUuid()));

                if (StringUtils.isNotBlank(message.getEmail())) {
                    log.info("Set player {} email {}", message.getPlayerUuid(), message.getEmail());
                    playerAccountService.setPlayerAccountEmailBySystemEvent(
                            UUID.fromString(message.getPlayerUuid()),
                            message.getEmail()
                    );
                } else {
                    log.info("Player {} email is not present", message.getPlayerUuid());
                }
            }
            case "u" -> {
                log.info("Process operation {} for player with uuid: {}", op, message.getPlayerUuid());

                if (StringUtils.isNotBlank(message.getEmail())) {
                    log.info("Set player {} email {}", message.getPlayerUuid(), message.getEmail());
                    playerAccountService.setPlayerAccountEmailBySystemEvent(
                            UUID.fromString(message.getPlayerUuid()),
                            message.getEmail()
                    );
                } else {
                    log.info("Player {} email is not present", message.getPlayerUuid());
                }
            }
            default -> log.info("Unprocessed operation {} for player with uuid: {}", op, message.getPlayerUuid());
        }

        log.info("Player account initialized for player with uuid: {}", message.getPlayerUuid());
    }
}
