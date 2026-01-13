package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter.InventoryEquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Embeddable
public class ReferenceEquipmentId implements Serializable {

    @Column(name = "equipment_type")
    @Convert(converter = InventoryEquipmentTypeConverter.class)
    private InventoryEquipmentType equipmentType;

    @Column(name = "equipment_id")
    private Integer equipmentId;
}
