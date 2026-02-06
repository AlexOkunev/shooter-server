package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;

@Data
@Entity
@Table(name = "ref_equipment")
@FieldNameConstants
public class ReferenceEquipment implements CacheableData<ReferenceEquipmentId> {

    @EmbeddedId
    private ReferenceEquipmentId id;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "name")
    private String name;
}