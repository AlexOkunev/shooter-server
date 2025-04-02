package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.TrimName;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionWritableData;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {GunReducedMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface AmmunitionMapper {

    @ConvertTimestampsToMs
    @Mapping(target = "compatibleGunsList", ignore = true)
    AmmunitionInfo toResponse(Ammunition source);

    @ConvertTimestampsToMs
    @Mapping(source = "compatibleGuns", target = "compatibleGunsList")
    AmmunitionInfo toResponseWithGuns(Ammunition source);

    @ConvertTimestampsToMs
    @Mapping(source = "enabledCompatibleGuns", target = "compatibleGunsList")
    AmmunitionInfo toResponseWithEnabledGuns(Ammunition source);

    @TrimName
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    Ammunition toEntity(AmmunitionWritableData source, List<Gun> compatibleGuns);

    @TrimName
    @SetUpdatedTimestamp
    void updateAmmunition(@MappingTarget Ammunition target, AmmunitionWritableData source, List<Gun> compatibleGuns);
}