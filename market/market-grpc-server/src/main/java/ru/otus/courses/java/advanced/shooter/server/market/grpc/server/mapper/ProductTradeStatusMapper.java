package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductTradeStatusMapper {

    @ValueMapping(target = "CREATED", source = "PT_CREATED")
    @ValueMapping(target = "WRITE_OFF_WAIT", source = "PT_WRITE_OFF_WAIT")
    @ValueMapping(target = "WRITE_OFF_PENDING", source = "PT_WRITE_OFF_PENDING")
    @ValueMapping(target = "WRITE_OFF_DONE", source = "PT_WRITE_OFF_DONE")
    @ValueMapping(target = "ISSUE_EQUIPMENT_PENDING", source = "PT_ISSUE_EQUIPMENT_PENDING")
    @ValueMapping(target = "ISSUE_EQUIPMENT_DONE", source = "PT_ISSUE_EQUIPMENT_DONE")
    @ValueMapping(target = "SUCCEEDED", source = "PT_SUCCEEDED")
    @ValueMapping(target = "FAILED", source = "PT_FAILED")
    @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
    ProductTradeStatus toEntity(ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.ProductTradeStatus source);

    List<ProductTradeStatus> toEntityList(List<ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.ProductTradeStatus> source);

    @InheritInverseConfiguration
    @ValueMapping(target = "UNRECOGNIZED", source = "UNKNOWN")
    ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.ProductTradeStatus toResponse(ProductTradeStatus source);
}
