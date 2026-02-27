package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunsFilter;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                PaginationInfoDtoMapper.class,
                AmmunitionReducedMapper.class,
                AttachmentReducedMapper.class
        }
)
public abstract class GunMapper {

    @Mapping(target = "compatibleAmmunitionList", source = "compatibleAmmunitionInfos")
    public abstract GunDto toDto(GunInfo gunInfo);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<GunDto> toDtoList(Collection<GunInfo> gunInfos);

    @Mapping(source = "data", target = "items")
    public abstract GunPageResponseDto toPageDto(GunInfoListPage gunInfoListPage);

    @Mapping(target = "enabled", constant = "true")
    public abstract GunsFilter toProto(GunSearchRequestDto requestDto);
}
