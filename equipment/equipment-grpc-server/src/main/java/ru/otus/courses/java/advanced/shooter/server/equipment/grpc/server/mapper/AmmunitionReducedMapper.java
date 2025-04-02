package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionReducedInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AmmunitionReducedMapper {
    AmmunitionReducedInfo toReducedResponse(Ammunition source);
}
