package ru.otus.courses.java.advanced.shooter.server.gateway.player.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "equipment-grpc-pool")
public class EquipmentGrpcBlockingPoolProperties {

    @Valid
    @NotNull
    private GrpcBlockingPoolProperties properties;
}
