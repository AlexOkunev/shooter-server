package ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.GrpcServerProperties;

@Data
@Validated
@ConfigurationProperties(prefix = "equipment-grpc-service")
public class EquipmentGrpcProperties {

    @Valid
    @NotNull
    private GrpcServerProperties server;

    @Positive
    private int deadlineMs;
}
