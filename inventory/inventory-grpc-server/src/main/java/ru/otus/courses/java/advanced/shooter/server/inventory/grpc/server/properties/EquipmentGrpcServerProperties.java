package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "equipment-service.grpc-server")
public class EquipmentGrpcServerProperties {
    @NotBlank
    private String host;

    @Positive
    @NotNull
    private Integer port;
}
