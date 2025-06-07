package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.ProductEquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@Data
@Entity
@Table(name = "ref_equipment")
@IdClass(ReferenceEquipmentId.class)
@FieldNameConstants
public class ReferenceEquipment implements CacheableData<ReferenceEquipmentId> {
    @Id
    @Column(name = "equipment_id")
    private Integer equipmentId;

    @Id
    @Column(name = "equipment_type")
    @Convert(converter = ProductEquipmentTypeConverter.class)
    private ProductEquipmentType equipmentType;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "name")
    private String name;

    @Override
    public ReferenceEquipmentId getId() {
        return new ReferenceEquipmentId(equipmentId, equipmentType);
    }
}