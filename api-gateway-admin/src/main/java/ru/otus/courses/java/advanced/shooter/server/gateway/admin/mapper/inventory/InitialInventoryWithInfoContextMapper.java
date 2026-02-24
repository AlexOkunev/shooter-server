package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.PlayerInventoryInfoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.*;

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
                EquipmentTypeMapper.class,
                GrenadeMapper.class,
                AmmunitionMapper.class,
                AttachmentMapper.class,
                GunMapper.class
        }
)
public abstract class InitialInventoryWithInfoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract InitialPlayerInventoryItemsPageResponseDto toPageDto(InitialPlayerInventoryItemsPage page,
                                                                         @Context PlayerInventoryInfoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<InitialPlayerInventoryItemDto> toDtoList(Collection<InitialPlayerInventoryItemInfo> items,
                                                                  @Context PlayerInventoryInfoMappingContext context);

    @Mapping(target = "equipment", source = ".")
    public abstract InitialPlayerInventoryItemDto toDto(InitialPlayerInventoryItemInfo item, @Context PlayerInventoryInfoMappingContext context);

    public abstract GetInitialPlayerInventoryRequest.Filter toProto(SearchInitialPlayerInventoryRequestDto dto);

    public abstract UpdateInitialPlayerInventoryRequest toProto(UpdateInitialPlayerInventoryRequestDto dto);

    protected InventoryEquipmentDto toEquipment(InitialPlayerInventoryItemInfo item, @Context PlayerInventoryInfoMappingContext context) {
        if (item == null) {
            return null;
        }

        return switch (item.getEquipmentType()) {
            case GUN -> toGunDto(context.gunsById().get(item.getEquipmentId()));
            case GRENADE -> toGrenadeDto(context.grenadesById().get(item.getEquipmentId()));
            case AMMUNITION -> toAmmunitionDto(context.ammunitionById().get(item.getEquipmentId()));
            case ATTACHMENT -> toAttachmentDto(context.attachmentsById().get(item.getEquipmentId()));
            default -> null;
        };
    }

    @Mapping(target = "grenade", source = ".")
    protected abstract GrenadeInventoryEquipmentDto toGrenadeDto(GrenadeInfo grenadeInfo);

    @Mapping(target = "ammunition", source = ".")
    protected abstract AmmunitionInventoryEquipmentDto toAmmunitionDto(AmmunitionInfo ammunitionInfo);

    @Mapping(target = "attachment", source = ".")
    protected abstract AttachmentInventoryEquipmentDto toAttachmentDto(AttachmentInfo ammunitionInfo);

    @Mapping(target = "gun", source = ".")
    protected abstract GunInventoryEquipmentDto toGunDto(GunInfo ammunitionInfo);

    protected abstract DeletedInitialPlayerInventoryItem toProto(DeletedInitialPlayerInventoryItemDto item);

    protected abstract SavedInitialPlayerInventoryItem toProto(SavedInitialPlayerInventoryItemDto item);
}