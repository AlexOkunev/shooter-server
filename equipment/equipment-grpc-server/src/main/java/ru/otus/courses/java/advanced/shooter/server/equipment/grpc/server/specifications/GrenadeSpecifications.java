package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;

@UtilityClass
public class GrenadeSpecifications {
    public static Specification<Grenade> byNameStartsWith(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Grenade.Fields.name)), name.toLowerCase() + "%");
    }

    public static Specification<Grenade> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(Grenade.Fields.enabled), enabled);
    }

    public static Specification<Grenade> byGrenadeIds(Iterable<Integer> grenadeIds) {
        return (root, query, builder) ->
                builder.in(root.get(Grenade.Fields.id)).value(grenadeIds);
    }
}