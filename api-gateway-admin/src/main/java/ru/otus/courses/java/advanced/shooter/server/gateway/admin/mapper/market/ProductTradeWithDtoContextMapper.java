package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.EquipmentDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.GetProductTradesRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

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
public abstract class ProductTradeWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract ProductTradePageResponseDto toPageDto(ProductTradeInfoListPage productTradeInfoListPage,
                                                          @Context CurrencyDtoMappingContext currencyDtoMappingContext,
                                                          @Context EquipmentDtoMappingContext equipmentMappingContext
    );

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<ProductTradeDto> toDtoList(Collection<ProductTradeInfo> items,
                                                    @Context CurrencyDtoMappingContext currencyDtoMappingContext,
                                                    @Context EquipmentDtoMappingContext equipmentMappingContext
    );

    @Mappings({
            @Mapping(
                    target = ProductTradeDto.Fields.priceCurrency,
                    source = "priceCurrencyId"
            ),
            @Mapping(
                    target = ProductTradeDto.Fields.equipment,
                    source = "."
            )
    })
    public abstract ProductTradeDto toDto(ProductTradeInfo item,
                                          @Context CurrencyDtoMappingContext currencyDtoMappingContext,
                                          @Context EquipmentDtoMappingContext equipmentMappingContext
    );

    public abstract GetProductTradesRequest.Filter toProto(ProductTradeSearchRequestDto requestDto);

    protected CurrencyDto getCurrencyDto(int currencyId, @Context CurrencyDtoMappingContext context) {
        return context.currenciesById().get(currencyId);
    }

    protected ProductEquipmentDto toEquipmentDto(ProductTradeInfo tradeInfo, @Context EquipmentDtoMappingContext context) {
        if (tradeInfo == null) {
            return null;
        }

        int equipmentId = tradeInfo.getEquipmentId();

        return switch (tradeInfo.getEquipmentType()) {
            case GUN -> new GunProductEquipmentDto(context.gunsById().get(equipmentId));
            case GRENADE -> new GrenadeProductEquipmentDto(context.grenadesById().get(equipmentId));
            case AMMUNITION -> new AmmunitionProductEquipmentDto(context.ammunitionById().get(equipmentId));
            case ATTACHMENT -> new AttachmentProductEquipmentDto(context.attachmentsById().get(equipmentId));
            default -> null;
        };
    }
}