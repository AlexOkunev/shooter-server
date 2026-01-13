package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Embeddable
public class PlayerInventoryItemId implements Serializable {

    @Column(name = "player_uuid")
    private UUID playerUuid;

    @Embedded
    private ReferenceEquipmentId referenceEquipmentId;
}
