package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@UtilityClass
public class ProductSpecifications {
    public static Specification<Product> byIds(Iterable<Integer> ids) {
        return (root, query, builder) ->
                builder.in(root.get(Product.Fields.id)).value(ids);
    }

    public static Specification<Product> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(Product.Fields.enabled), enabled);
    }

    public static Specification<Product> byPriceCurrencyEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.join(Product.Fields.priceCurrency).get(ReferenceCurrency.Fields.enabled), enabled);
    }

    public static Specification<Product> byPriceCurrencyIds(Iterable<Integer> priceCurrencyIds) {
        return (root, query, builder) ->
                builder.in(root.get(Product.Fields.priceCurrencyId)).value(priceCurrencyIds);
    }

    public static Specification<Product> byPriceGte(int priceLowLimit) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get(Product.Fields.price), priceLowLimit);
    }

    public static Specification<Product> byPriceLte(int priceHighLimit) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get(Product.Fields.price), priceHighLimit);
    }

    public static Specification<Product> byEquipmentEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.join(Product.Fields.equipment).get(ReferenceEquipment.Fields.enabled), enabled);
    }

    public static Specification<Product> byEquipmentIds(Iterable<Integer> equipmentIds) {
        return (root, query, builder) ->
                builder.in(root.get(Product.Fields.equipmentId)).value(equipmentIds);
    }

    public static Specification<Product> byEquipmentTypes(Iterable<ProductEquipmentType> equipmentTypes) {
        return (root, query, builder) ->
                builder.in(root.get(Product.Fields.equipmentType)).value(equipmentTypes);
    }

    public static Specification<Product> byEquipmentAmountGte(int limit) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get(Product.Fields.equipmentAmount), limit);
    }

    public static Specification<Product> byEquipmentAmountLte(int limit) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get(Product.Fields.equipmentAmount), limit);
    }
}