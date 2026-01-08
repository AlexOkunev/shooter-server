package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.ZonedDateTime;
import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class GunFilterParams {

    Boolean enabled;
    String name;
    GunType type;
    ZonedDateTime updatedAfter;

    @Builder.Default
    Set<Integer> gunIds = Set.of();
}
