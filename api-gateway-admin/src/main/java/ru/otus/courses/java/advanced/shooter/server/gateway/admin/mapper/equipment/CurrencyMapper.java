package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrenciesFilter;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyWritableData;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencySaveRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencySearchRequestDto;
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
public abstract class CurrencyMapper {

    public abstract CurrencyDto toDto(CurrencyInfo currencyInfo);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CurrencyDto> toDtoList(Collection<CurrencyInfo> currencyInfos);

    @Mapping(source = "data", target = "items")
    public abstract PageResponseDto<CurrencyDto> toPageDto(CurrencyInfoListPage currencyInfoListPage);

    public abstract CurrenciesFilter toProto(CurrencySearchRequestDto requestDto);

    public abstract CurrencyWritableData toProto(CurrencySaveRequestDto requestDto);
}
