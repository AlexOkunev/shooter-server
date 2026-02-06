package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.IgnoreCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUuid;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.outbox.ProductTradeIssueRequiredMessage;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ProductTradeIssueRequiredMessageMapper {

    @IgnoreCreatedTimestamp
    @SetUuid
    @Mappings({
            @Mapping(
                    target = ProductTradeIssueRequiredMessage.Fields.tradeUuid,
                    source = ProductTrade.Fields.uuid
            ),
            @Mapping(
                    target = ProductTradeIssueRequiredMessage.Fields.equipmentId,
                    source = ProductTrade.Fields.productEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentId
            ),
            @Mapping(
                    target = ProductTradeIssueRequiredMessage.Fields.equipmentType,
                    source = ProductTrade.Fields.productEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentType
            )
    })
    ProductTradeIssueRequiredMessage toMessage(ProductTrade trade);
}
