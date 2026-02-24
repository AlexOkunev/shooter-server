package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.EquipmentTypeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.PlayerInventoryDtoMappingContext;
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
                EquipmentTypeMapper.class
        }
)
public abstract class InitialInventoryWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract InitialPlayerInventoryItemsPageResponseDto toPageDto(InitialPlayerInventoryItemsPage page,
                                                                         @Context PlayerInventoryDtoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<InitialPlayerInventoryItemDto> toDtoList(Collection<InitialPlayerInventoryItemInfo> items,
                                                                  @Context PlayerInventoryDtoMappingContext context);

    @Mapping(target = "equipment", source = ".")
    public abstract InitialPlayerInventoryItemDto toDto(InitialPlayerInventoryItemInfo item, @Context PlayerInventoryDtoMappingContext context);

    public abstract GetInitialPlayerInventoryRequest.Filter toProto(SearchInitialPlayerInventoryRequestDto dto);

    public abstract UpdateInitialPlayerInventoryRequest toProto(UpdateInitialPlayerInventoryRequestDto dto);

    protected InventoryEquipmentDto toEquipmentDto(InitialPlayerInventoryItemInfo itemInfo, @Context PlayerInventoryDtoMappingContext context) {
        if (itemInfo == null) {
            return null;
        }

        int equipmentId = itemInfo.getEquipmentId();

        return switch (itemInfo.getEquipmentType()) {
            case GUN -> new GunInventoryEquipmentDto(context.gunsById().get(equipmentId));
            case GRENADE -> new GrenadeInventoryEquipmentDto(context.grenadesById().get(equipmentId));
            case AMMUNITION -> new AmmunitionInventoryEquipmentDto(context.ammunitionById().get(equipmentId));
            case ATTACHMENT -> new AttachmentInventoryEquipmentDto(context.attachmentsById().get(equipmentId));
            default -> null;
        };
    }

    protected abstract DeletedInitialPlayerInventoryItem toProto(DeletedInitialPlayerInventoryItemDto item);

    protected abstract SavedInitialPlayerInventoryItem toProto(SavedInitialPlayerInventoryItemDto item);
}