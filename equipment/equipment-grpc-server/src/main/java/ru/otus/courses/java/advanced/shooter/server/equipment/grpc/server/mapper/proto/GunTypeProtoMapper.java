package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.ThrowOnUnrecognized;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GunTypeProtoMapper {
    @ThrowOnUnrecognized
    @ValueMapping(source = "GUN_TYPE_UNSPECIFIED", target = MappingConstants.THROW_EXCEPTION)
    GunType toBean(
            ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunType proto
    );

    ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunType toProto(
            GunType bean
    );
}
