package ru.otus.courses.java.advanced.shooter.server.gateway.player.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "player-grpc-service")
public class PlayerGrpcProperties {

    @Valid
    @NotNull
    private GrpcServerProperties server;

    @Positive
    private int deadlineMs;
}
