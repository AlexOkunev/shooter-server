package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.ThrowOnUnrecognized;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RelatedEntitiesInclusionModeProtoMapper {
    @ThrowOnUnrecognized
    RelatedEntitiesInclusionMode toBean(
            ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode proto
    );
}
