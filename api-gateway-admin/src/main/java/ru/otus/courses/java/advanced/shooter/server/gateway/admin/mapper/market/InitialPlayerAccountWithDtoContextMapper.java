package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.*;

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
public abstract class InitialPlayerAccountWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract InitialPlayerAccountItemsPageResponseDto toPageDto(InitialPlayerAccountItemsPage page,
                                                                       @Context CurrencyDtoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<InitialPlayerAccountItemDto> toDtoList(Collection<InitialPlayerAccountItemInfo> items,
                                                                @Context CurrencyDtoMappingContext context);

    @Mapping(target = "currency", source = "currencyId")
    public abstract InitialPlayerAccountItemDto toDto(InitialPlayerAccountItemInfo item, @Context CurrencyDtoMappingContext context);

    protected CurrencyDto getCurrencyDto(int currencyId, @Context CurrencyDtoMappingContext context) {
        return context.currenciesById().get(currencyId);
    }

    public abstract GetInitialPlayerAccountRequest.Filter toProto(InitialPlayerAccountSearchRequestDto dto);

    public abstract UpdateInitialPlayerAccountRequest toProto(UpdateInitialPlayerAccountRequestDto dto);

    protected abstract SavedInitialPlayerAccountItemRequest toProto(SavedInitialPlayerAccountItemDto item);
}