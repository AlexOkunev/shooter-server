package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.outbox;


import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class PlayerBoundMessageId implements Serializable {

    private UUID playerUuid;

    private UUID messageUuid;
}
