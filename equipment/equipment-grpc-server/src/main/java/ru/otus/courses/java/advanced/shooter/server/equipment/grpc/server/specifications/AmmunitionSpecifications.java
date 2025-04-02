package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

@UtilityClass
public class AmmunitionSpecifications {
    public static Specification<Ammunition> byNameStartsWith(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Ammunition.Fields.name)), name.toLowerCase() + "%");
    }

    public static Specification<Ammunition> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(Ammunition.Fields.enabled), enabled);
    }

    public static Specification<Ammunition> byCompatibleGunIds(Iterable<Integer> compatibleGunIds) {
        return (root, query, builder) ->
                builder.in(root.join(Ammunition.Fields.compatibleGuns).get(Gun.Fields.id)).value(compatibleGunIds);
    }

    public static Specification<Ammunition> byEnabledCompatibleGunIds(Iterable<Integer> compatibleGunIds) {
        return (root, query, builder) ->
                builder.and(
                        builder.in(root.join(Ammunition.Fields.compatibleGuns).get(Gun.Fields.id)).value(compatibleGunIds),
                        builder.equal(root.join(Ammunition.Fields.compatibleGuns).get(Gun.Fields.enabled), true)
                );
    }

    public static Specification<Ammunition> byAmmunitionIds(Iterable<Integer> ammunitionIds) {
        return (root, query, builder) ->
                builder.in(root.get(Ammunition.Fields.id)).value(ammunitionIds);
    }
}