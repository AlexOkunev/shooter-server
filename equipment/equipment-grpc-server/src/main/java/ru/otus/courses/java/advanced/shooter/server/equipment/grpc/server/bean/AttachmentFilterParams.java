package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.ZonedDateTime;
import java.util.Set;

@Value
@Builder
@FieldNameConstants
public class AttachmentFilterParams {

    Boolean enabled;
    String name;
    AttachmentType type;
    ZonedDateTime updatedAfter;

    boolean onlyEnabledCompatibleGuns;

    @Builder.Default
    Set<Integer> compatibleGunIds = Set.of();

    @Builder.Default
    Set<Integer> attachmentIds = Set.of();
}
