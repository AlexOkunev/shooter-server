package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyInfoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.helper.CurrencyMappingHelper;
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
                PaginationInfoDtoMapper.class,
                CurrencyMappingHelper.class
        }
)
public abstract class InitialPlayerAccountWithInfoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract InitialPlayerAccountItemsPageResponseDto toPageDto(InitialPlayerAccountItemsPage page,
                                                                       @Context CurrencyInfoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<InitialPlayerAccountItemDto> toDtoList(Collection<InitialPlayerAccountItemInfo> items,
                                                                @Context CurrencyInfoMappingContext context);

    @Mapping(
            target = "currency",
            source = "currencyId",
            qualifiedByName = CurrencyMappingHelper.NamedMethods.NAMED_TO_CURRENCY_DTO
    )
    public abstract InitialPlayerAccountItemDto toDto(InitialPlayerAccountItemInfo item, @Context CurrencyInfoMappingContext context);

    public abstract GetInitialPlayerAccountRequest.Filter toProto(InitialPlayerAccountSearchRequestDto dto);

    public abstract UpdateInitialPlayerAccountRequest toProto(UpdateInitialPlayerAccountRequestDto dto);

    protected abstract SavedInitialPlayerAccountItemRequest toProto(SavedInitialPlayerAccountItemDto item);
}