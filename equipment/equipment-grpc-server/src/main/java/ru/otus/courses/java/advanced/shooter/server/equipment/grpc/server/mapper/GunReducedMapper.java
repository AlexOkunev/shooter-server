package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunReducedInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GunReducedMapper {
    GunReducedInfo toReducedResponse(Gun source);
}
