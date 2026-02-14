package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.OperationType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationTypeProtoMapper {

    ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.OperationType toResponse(OperationType source);
}
