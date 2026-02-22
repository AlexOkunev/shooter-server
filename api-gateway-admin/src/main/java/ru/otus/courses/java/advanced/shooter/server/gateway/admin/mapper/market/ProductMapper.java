package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.AmmunitionMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.AttachmentMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.GrenadeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.GunMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.EquipmentMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.helper.CurrencyMappingHelper;
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
                PaginationInfoDtoMapper.class,
                CurrencyMappingHelper.class,
                GrenadeMapper.class,
                AmmunitionMapper.class,
                AttachmentMapper.class,
                GunMapper.class
        }
)
public abstract class ProductMapper {

    @Mapping(source = "data", target = "items")
    public abstract ProductPageResponseDto toPageDto(ProductInfoListPage productInfoListPage,
                                                     @Context CurrencyMappingContext currencyMappingContext,
                                                     @Context EquipmentMappingContext equipmentMappingContext
    );

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<ProductDto> toDtoList(Collection<ProductInfo> items,
                                               @Context CurrencyMappingContext currencyMappingContext,
                                               @Context EquipmentMappingContext equipmentMappingContext
    );

    @Mappings({
            @Mapping(
                    target = ProductDto.Fields.priceCurrency,
                    source = "price.currencyId",
                    qualifiedByName = CurrencyMappingHelper.NamedMethods.NAMED_TO_CURRENCY_DTO
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
                                     @Context CurrencyMappingContext currencyMappingContext,
                                     @Context EquipmentMappingContext equipmentMappingContext
    );

    protected ProductEquipmentDto toEquipment(ProductEquipmentInfo equipment, @Context EquipmentMappingContext context) {
        if (equipment == null) {
            return null;
        }

        return switch (equipment.getEquipmentType()) {
            case GUN -> toGunDto(context.gunsById().get(equipment.getEquipmentId()));
            case GRENADE -> toGrenadeDto(context.grenadesById().get(equipment.getEquipmentId()));
            case AMMUNITION -> toAmmunitionDto(context.ammunitionById().get(equipment.getEquipmentId()));
            case ATTACHMENT -> toAttachmentDto(context.attachmentsById().get(equipment.getEquipmentId()));
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

    @Mapping(target = "price", source = ".")
    @Mapping(target = "equipment", source = ".")
    public abstract ProductWritableData toProto(SaveProductRequestDto dto);

    @Mapping(target = "currencyId", source = "priceCurrencyId")
    @Mapping(target = "amount", source = "priceValue")
    protected abstract PriceInfo toPriceInfoProto(SaveProductRequestDto dto);

    @Mapping(target = "amount", source = "equipmentAmount")
    protected abstract ProductEquipmentInfo toProductEquipmentInfoProto(SaveProductRequestDto dto);
}