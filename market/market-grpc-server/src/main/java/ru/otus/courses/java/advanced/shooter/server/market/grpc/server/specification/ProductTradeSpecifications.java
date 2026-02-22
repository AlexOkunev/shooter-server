package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

import java.util.UUID;

@UtilityClass
public class ProductTradeSpecifications {

    public static Specification<ProductTrade> byPlayerUuid(UUID playerUuid) {
        return (root, query, builder) ->
                builder.equal(root.get(ProductTrade.Fields.playerUuid), playerUuid);
    }

    public static Specification<ProductTrade> byEquipmentTypeAndEquipmentIds(ProductEquipmentType equipmentType, Iterable<Integer> equipmentIds) {
        return (root, query, builder) ->
                builder.and(
                        builder.equal(root.get(ProductTrade.Fields.productEquipmentId)
                                .get(ReferenceEquipmentId.Fields.equipmentType), equipmentType),
                        builder.in(root.get(ProductTrade.Fields.productEquipmentId)
                                .get(ReferenceEquipmentId.Fields.equipmentId)).value(equipmentIds)
                );
    }

    public static Specification<ProductTrade> byProductIds(Iterable<Integer> productIds) {
        return (root, query, builder) ->
                builder.in(root.get(ProductTrade.Fields.productId)).value(productIds);
    }

    public static Specification<ProductTrade> byStatuses(Iterable<ProductTradeStatus> statuses) {
        return (root, query, builder) ->
                builder.in(root.get(ProductTrade.Fields.status)).value(statuses);
    }
}