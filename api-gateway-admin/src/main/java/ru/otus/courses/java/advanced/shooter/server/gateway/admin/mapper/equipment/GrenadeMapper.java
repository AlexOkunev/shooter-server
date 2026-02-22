package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadesFilter;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;

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
                PaginationInfoDtoMapper.class
        }
)
public abstract class GrenadeMapper {

    public abstract GrenadeDto toDto(GrenadeInfo grenadeInfo);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<GrenadeDto> toDtoList(Collection<GrenadeInfo> grenadeInfos);

    @Mapping(source = "data", target = "items")
    public abstract PageResponseDto<GrenadeDto> toPageDto(GrenadeInfoListPage grenadeInfoListPage);

    @Mapping(target = "enabled", constant = "true")
    public abstract GrenadesFilter toProto(GrenadeSearchRequestDto requestDto);
}
