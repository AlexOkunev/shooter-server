package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class PlayerAccountItemId implements Serializable {
    private UUID playerUuid;
    private Integer currencyId;
}
