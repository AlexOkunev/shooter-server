package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerInventoryLogEntryId {

    private UUID playerUuid;

    private UUID uuid;
}
