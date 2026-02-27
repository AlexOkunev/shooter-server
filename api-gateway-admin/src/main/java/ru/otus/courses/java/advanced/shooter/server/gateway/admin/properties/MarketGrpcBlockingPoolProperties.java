package ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "market-grpc-pool")
public class MarketGrpcBlockingPoolProperties {

    @Valid
    @NotNull
    private GrpcBlockingPoolProperties properties;
}
