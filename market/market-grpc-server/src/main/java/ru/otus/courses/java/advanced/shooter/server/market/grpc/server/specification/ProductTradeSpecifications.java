package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

import java.util.stream.StreamSupport;

@UtilityClass
public class ProductTradeSpecifications {
    public static Specification<ProductTrade> byIds(Iterable<Integer> ids) {
        return (root, query, builder) ->
                builder.in(root.get(ProductTrade.Fields.id)).value(ids);
    }

    public static Specification<ProductTrade> byPlayerId(Integer playerId) {
        return (root, query, builder) ->
                builder.equal(root.get(ProductTrade.Fields.playerId), playerId);
    }

    public static Specification<ProductTrade> byEquipmentTypeAndEquipmentIds(ProductEquipmentType equipmentType, Iterable<Integer> equipmentIds) {
        return (root, query, builder) ->
                builder.and(
                        builder.equal(root.get(ProductTrade.Fields.productEquipmentType), equipmentType.getCode()),
                        builder.in(root.get(ProductTrade.Fields.productEquipmentId)).value(equipmentIds)
                );
    }

    public static Specification<ProductTrade> byProductIds(Iterable<Integer> productIds) {
        return (root, query, builder) ->
                builder.in(root.join(ProductTrade.Fields.productId)).value(productIds);
    }

    public static Specification<ProductTrade> byStatuses(Iterable<ProductTradeStatus> statuses) {
        Iterable<Integer> statusCodes = StreamSupport.stream(statuses.spliterator(), false)
                .map(ProductTradeStatus::getCode)
                .toList();

        return (root, query, builder) ->
                builder.in(root.join(ProductTrade.Fields.status)).value(statusCodes);
    }
}