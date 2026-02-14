package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAccountLogEntryId implements Serializable {

    private UUID playerUuid;

    private UUID uuid;
}
