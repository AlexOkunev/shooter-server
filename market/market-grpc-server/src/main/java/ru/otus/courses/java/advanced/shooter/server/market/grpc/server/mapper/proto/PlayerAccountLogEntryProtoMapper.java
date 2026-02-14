package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountLogEntry;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.GetPlayerAccountLogRequest;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                EquipmentTypeProtoMapper.class,
                DateMapper.class,
                OperationTypeProtoMapper.class
        }
)
public abstract class PlayerAccountLogEntryProtoMapper {

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogEntry> toResponseList(
            Iterable<PlayerAccountLogEntry> source);

    public abstract ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogEntry toResponse(PlayerAccountLogEntry source);

    public Pageable toPageable(GetPlayerAccountLogRequest request) {
        Sort sort = Sort.by(Sort.Direction.ASC, PlayerAccountLogEntry.Fields.timestamp);

        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, sort);
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                sort
        );
    }
}
