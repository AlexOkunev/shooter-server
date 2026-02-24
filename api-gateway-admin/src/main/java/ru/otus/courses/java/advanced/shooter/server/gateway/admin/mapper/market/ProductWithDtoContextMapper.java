package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.EquipmentDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

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
public abstract class ProductWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract ProductPageResponseDto toPageDto(ProductInfoListPage productInfoListPage,
                                                     @Context CurrencyDtoMappingContext currencyDtoMappingContext,
                                                     @Context EquipmentDtoMappingContext equipmentMappingContext
    );

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<ProductDto> toDtoList(Collection<ProductInfo> items,
                                               @Context CurrencyDtoMappingContext currencyDtoMappingContext,
                                               @Context EquipmentDtoMappingContext equipmentMappingContext
    );

    @Mappings({
            @Mapping(
                    target = ProductDto.Fields.priceCurrency,
                    source = "price.currencyId"
            ),
            @Mapping(
                    target = ProductDto.Fields.equipmentType,
                    source = "equipment.equipmentType"
            ),
            @Mapping(
                    target = ProductDto.Fields.equipmentAmount,
                    source = "equipment.amount"
            ),
            @Mapping(
                    target = ProductDto.Fields.priceValue,
                    source = "price.amount"
            )
    })
    public abstract ProductDto toDto(ProductInfo item,
                                     @Context CurrencyDtoMappingContext currencyDtoMappingContext,
                                     @Context EquipmentDtoMappingContext equipmentMappingContext
    );

    protected CurrencyDto getCurrencyDto(int currencyId, @Context CurrencyDtoMappingContext context) {
        return context.currenciesById().get(currencyId);
    }

    protected ProductEquipmentDto toEquipmentDto(ProductEquipmentInfo equipment, @Context EquipmentDtoMappingContext context) {
        if (equipment == null) {
            return null;
        }

        int equipmentId = equipment.getEquipmentId();

        return switch (equipment.getEquipmentType()) {
            case GUN -> new GunProductEquipmentDto(context.gunsById().get(equipmentId));
            case GRENADE -> new GrenadeProductEquipmentDto(context.grenadesById().get(equipmentId));
            case AMMUNITION -> new AmmunitionProductEquipmentDto(context.ammunitionById().get(equipmentId));
            case ATTACHMENT -> new AttachmentProductEquipmentDto(context.attachmentsById().get(equipmentId));
            default -> null;
        };
    }

    @Mapping(target = "price", source = ".")
    @Mapping(target = "equipment", source = ".")
    public abstract ProductWritableData toProto(SaveProductRequestDto dto);

    @Mapping(target = "currencyId", source = "priceCurrencyId")
    @Mapping(target = "amount", source = "priceValue")
    protected abstract PriceInfo toPriceInfoProto(SaveProductRequestDto dto);

    @Mapping(target = "amount", source = "equipmentAmount")
    protected abstract ProductEquipmentInfo toProductEquipmentInfoProto(SaveProductRequestDto dto);
}