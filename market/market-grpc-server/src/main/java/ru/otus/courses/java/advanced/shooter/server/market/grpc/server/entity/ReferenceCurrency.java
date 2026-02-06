package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;

@Entity
@Table(name = "ref_currency")
@Data
@FieldNameConstants
public class ReferenceCurrency implements CacheableData<Integer> {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "name")
    private String name;

    @Column(name = "can_be_bought")
    private boolean canBeBought;
}