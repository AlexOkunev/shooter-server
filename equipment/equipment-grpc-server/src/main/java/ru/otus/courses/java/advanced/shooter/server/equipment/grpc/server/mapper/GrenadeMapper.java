package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.TrimName;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeWritableData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrenadeMapper {
    @ConvertTimestampsToMs
    GrenadeInfo toResponse(Grenade source);

    @TrimName
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    Grenade toEntity(GrenadeWritableData source);

    @TrimName
    @SetUpdatedTimestamp
    void updateGrenade(@MappingTarget Grenade grenade, GrenadeWritableData source);
}
