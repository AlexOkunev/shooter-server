package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerEquipmentOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerInventoryItemFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItemId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerEquipmentOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemInfo;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                EquipmentTypeProtoMapper.class,
                DateMapper.class
        }
)
public abstract class PlayerInventoryProtoMapper {
    private static final class ProtoFields {
        public static final String EQUIPMENT_ID = "equipmentId";
        public static final String EQUIPMENT_TYPE = "equipmentType";
    }

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<PlayerInventoryItemInfo> toResponseList(Iterable<PlayerInventoryItem> source);

    @Mappings({
            @Mapping(
                    target = ProtoFields.EQUIPMENT_ID,
                    source = PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentId
            ),
            @Mapping(
                    target = ProtoFields.EQUIPMENT_TYPE,
                    source = InitialPlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentType
            )
    })
    public abstract PlayerInventoryItemInfo toResponse(PlayerInventoryItem source);

    public abstract PlayerEquipmentOperationCommand toCommand(PlayerEquipmentOperationRequest request);

    public Pageable toPageable(GetPlayerInventoryRequest request) {
        Sort sort = Sort.by(
                Sort.Order.asc(PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentType),
                Sort.Order.asc(PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentId)
        );

        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, sort);
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                sort
        );
    }

    public PlayerInventoryItemFilterParams toFilterParams(GetPlayerInventoryRequest request) {
        if (!request.hasFilter()) {
            return PlayerInventoryItemFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    protected abstract PlayerInventoryItemFilterParams toFilterParamsInternal(GetPlayerInventoryRequest.Filter filter);
}