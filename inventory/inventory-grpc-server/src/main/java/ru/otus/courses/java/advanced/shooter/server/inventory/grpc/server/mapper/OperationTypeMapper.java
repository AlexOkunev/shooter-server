package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationTypeMapper {
    @ValueMapping(target = "UNKNOWN", source = "UNRECOGNIZED")
    @ValueMapping(target = "ADMIN_GIVE", source = "ADMIN_GIVE")
    @ValueMapping(target = "ADMIN_TAKE_AWAY", source = "ADMIN_TAKE_AWAY")
    @ValueMapping(target = "SPEND", source = "SPEND")
    @ValueMapping(target = "BUY", source = "BUY")
    @ValueMapping(target = "GIVE_AWARD", source = "GIVE_AWARD")
    @ValueMapping(target = "SELL", source = "SELL")
    @ValueMapping(target = "HOLD", source = "HOLD")
    OperationType toEntity(ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.OperationType source);

    @InheritInverseConfiguration
    ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.OperationType toResponse(OperationType source);
}
