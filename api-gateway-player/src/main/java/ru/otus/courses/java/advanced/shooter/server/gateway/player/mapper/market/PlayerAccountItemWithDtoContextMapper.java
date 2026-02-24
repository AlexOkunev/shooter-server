package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountItemDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountItemPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;

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
public abstract class PlayerAccountItemWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract PlayerAccountItemPageResponseDto toPageDto(PlayerAccountItemsPage playerInventoryLogPage,
                                                               @Context CurrencyDtoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<PlayerAccountItemDto> toDtoList(Collection<PlayerAccountItemInfo> items,
                                                         @Context CurrencyDtoMappingContext context);

    @Mapping(
            target = "currency",
            source = "currencyId"
    )
    public abstract PlayerAccountItemDto toDto(PlayerAccountItemInfo item, @Context CurrencyDtoMappingContext context);

    protected CurrencyDto getCurrencyDto(int currencyId, @Context CurrencyDtoMappingContext context) {
        return context.currenciesById().get(currencyId);
    }
}