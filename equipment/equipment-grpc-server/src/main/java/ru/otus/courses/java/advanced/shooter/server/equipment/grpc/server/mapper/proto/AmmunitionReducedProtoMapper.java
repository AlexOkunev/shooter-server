package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionReducedInfo;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class
        }
)
public abstract class AmmunitionReducedProtoMapper {
    public abstract AmmunitionReducedInfo toReducedResponse(Ammunition source);
}