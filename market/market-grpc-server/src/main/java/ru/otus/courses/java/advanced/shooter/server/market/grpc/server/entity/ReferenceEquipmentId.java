package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.ProductEquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Embeddable
public class ReferenceEquipmentId implements Serializable {

    @NotNull
    @Column(name = "equipment_type")
    @Convert(converter = ProductEquipmentTypeConverter.class)
    private ProductEquipmentType equipmentType;

    @NotNull
    @Column(name = "equipment_id")
    private Integer equipmentId;
}
