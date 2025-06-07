package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InitialPlayerAccountItemMapper {
    @Mapping(target = InitialPlayerAccountItem.Fields.version, ignore = true)
    InitialPlayerAccountItem toEntity(InitialPlayerAccountItemRequest source);

    @ConvertTimestampsToMs
    InitialPlayerAccountItemInfo toResponse(InitialPlayerAccountItem source);
}
