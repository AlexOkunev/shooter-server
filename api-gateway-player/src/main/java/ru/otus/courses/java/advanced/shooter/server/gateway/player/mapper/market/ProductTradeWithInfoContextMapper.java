package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.AmmunitionMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.AttachmentMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GrenadeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GunMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.CurrencyInfoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.EquipmentInfoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.helper.CurrencyMappingHelper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;
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
                PaginationInfoDtoMapper.class,
                CurrencyMappingHelper.class,
                GrenadeMapper.class,
                AmmunitionMapper.class,
                AttachmentMapper.class,
                GunMapper.class
        }
)
public abstract class ProductTradeWithInfoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract ProductTradePageResponseDto toPageDto(ProductTradeInfoListPage productTradeInfoListPage,
                                                          @Context CurrencyInfoMappingContext currencyMappingContext,
                                                          @Context EquipmentInfoMappingContext equipmentMappingContext
    );

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<ProductTradeDto> toDtoList(Collection<ProductTradeInfo> items,
                                                    @Context CurrencyInfoMappingContext currencyMappingContext,
                                                    @Context EquipmentInfoMappingContext equipmentMappingContext
    );

    @Mappings({
            @Mapping(
                    target = ProductTradeDto.Fields.priceCurrency,
                    source = "priceCurrencyId",
                    qualifiedByName = CurrencyMappingHelper.NamedMethods.NAMED_TO_CURRENCY_DTO
            ),
            @Mapping(
                    target = ProductTradeDto.Fields.equipment,
                    source = "."
            )
    })
    public abstract ProductTradeDto toDto(ProductTradeInfo item,
                                          @Context CurrencyInfoMappingContext currencyMappingContext,
                                          @Context EquipmentInfoMappingContext equipmentMappingContext
    );

    public abstract GetProductTradesRequest.Filter toProto(ProductTradeSearchRequestDto requestDto);

    protected ProductEquipmentDto toEquipment(ProductTradeInfo productTradeInfo, @Context EquipmentInfoMappingContext context) {
        if (productTradeInfo == null) {
            return null;
        }

        return switch (productTradeInfo.getEquipmentType()) {
            case GUN -> toGunDto(context.gunsById().get(productTradeInfo.getEquipmentId()));
            case GRENADE -> toGrenadeDto(context.grenadesById().get(productTradeInfo.getEquipmentId()));
            case AMMUNITION -> toAmmunitionDto(context.ammunitionById().get(productTradeInfo.getEquipmentId()));
            case ATTACHMENT -> toAttachmentDto(context.attachmentsById().get(productTradeInfo.getEquipmentId()));
            default -> null;
        };
    }

    @Mapping(target = "grenade", source = ".")
    protected abstract GrenadeProductEquipmentDto toGrenadeDto(GrenadeInfo grenadeInfo);

    @Mapping(target = "ammunition", source = ".")
    protected abstract AmmunitionProductEquipmentDto toAmmunitionDto(AmmunitionInfo ammunitionInfo);

    @Mapping(target = "attachment", source = ".")
    protected abstract AttachmentProductEquipmentDto toAttachmentDto(AttachmentInfo ammunitionInfo);

    @Mapping(target = "gun", source = ".")
    protected abstract GunProductEquipmentDto toGunDto(GunInfo ammunitionInfo);
}