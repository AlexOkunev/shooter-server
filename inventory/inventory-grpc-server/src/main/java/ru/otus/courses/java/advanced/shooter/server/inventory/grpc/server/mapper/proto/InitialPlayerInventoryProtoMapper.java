package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.DeletedInitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.InitialPlayerInventoryFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.SavedInitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.UpdateInitialPlayerInventoryCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemInfo;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

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
public abstract class InitialPlayerInventoryProtoMapper {
    private static final class ProtoFields {
        public static final String EQUIPMENT_ID = "equipmentId";
        public static final String EQUIPMENT_TYPE = "equipmentType";
    }

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<InitialPlayerInventoryItemInfo> toResponseList(Iterable<InitialPlayerInventoryItem> source);

    @Mappings({
            @Mapping(
                    target = ProtoFields.EQUIPMENT_ID,
                    source = InitialPlayerInventoryItem.Fields.id + "." + ReferenceEquipmentId.Fields.equipmentId
            ),
            @Mapping(
                    target = ProtoFields.EQUIPMENT_TYPE,
                    source = InitialPlayerInventoryItem.Fields.id + "." + ReferenceEquipmentId.Fields.equipmentType
            )
    })
    public abstract InitialPlayerInventoryItemInfo toResponse(InitialPlayerInventoryItem source);

    public abstract UpdateInitialPlayerInventoryCommand toCommand(UpdateInitialPlayerInventoryRequest request);

    public Pageable toPageable(GetInitialPlayerInventoryRequest request) {
        Sort sort = Sort.by(
                Sort.Order.asc(InitialPlayerInventoryItem.Fields.id + "." + ReferenceEquipmentId.Fields.equipmentType),
                Sort.Order.asc(InitialPlayerInventoryItem.Fields.id + "." + ReferenceEquipmentId.Fields.equipmentId)
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

    public InitialPlayerInventoryFilterParams toFilterParams(GetInitialPlayerInventoryRequest request) {
        if (!request.hasFilter()) {
            return InitialPlayerInventoryFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    protected abstract InitialPlayerInventoryFilterParams toFilterParamsInternal(GetInitialPlayerInventoryRequest.Filter request);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    protected abstract List<DeletedInitialPlayerInventoryItem> toDeletedItems(Iterable<ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.DeletedInitialPlayerInventoryItem> info);

    protected abstract DeletedInitialPlayerInventoryItem toDeletedItem(ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.DeletedInitialPlayerInventoryItem info);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    protected abstract List<SavedInitialPlayerInventoryItem> toSavedItems(Iterable<ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.SavedInitialPlayerInventoryItem> info);

    protected abstract SavedInitialPlayerInventoryItem toSavedItem(ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.SavedInitialPlayerInventoryItem info);
}
