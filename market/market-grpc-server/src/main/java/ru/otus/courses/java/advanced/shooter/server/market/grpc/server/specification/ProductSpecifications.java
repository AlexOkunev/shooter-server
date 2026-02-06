package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@UtilityClass
public class ProductSpecifications {

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

    public static Specification<Product> byEquipmentEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.join(Product.Fields.equipment).get(ReferenceEquipment.Fields.enabled), enabled);
    }

    public static Specification<Product> byEquipmentTypes(Iterable<ProductEquipmentType> equipmentTypes) {
        return (root, query, builder) ->
                builder.in(root.get(Product.Fields.equipmentId)
                                .get(ReferenceEquipmentId.Fields.equipmentType))
                        .value(equipmentTypes);
    }

}