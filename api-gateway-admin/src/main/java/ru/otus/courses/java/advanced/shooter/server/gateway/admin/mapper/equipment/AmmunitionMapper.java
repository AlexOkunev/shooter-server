package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionFilter;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionWritableData;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionSaveRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionSearchRequestDto;
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
                PaginationInfoDtoMapper.class,
                GunReducedMapper.class
        }
)
public abstract class AmmunitionMapper {

    public abstract AmmunitionDto toDto(AmmunitionInfo ammunitionInfo);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<AmmunitionDto> toDtoList(Collection<AmmunitionInfo> ammunitionInfos);

    @Mapping(source = "data", target = "items")
    public abstract PageResponseDto<AmmunitionDto> toPageDto(AmmunitionInfoListPage ammunitionInfoListPage);

    public abstract AmmunitionFilter toProto(AmmunitionSearchRequestDto requestDto);

    public abstract AmmunitionWritableData toProto(AmmunitionSaveRequestDto requestDto);
}
