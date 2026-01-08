package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunType;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

import java.time.ZonedDateTime;

@UtilityClass
public class GunSpecifications {
    public static Specification<Gun> byNameStartsWith(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Gun.Fields.name)), name.toLowerCase() + "%");
    }

    public static Specification<Gun> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(Gun.Fields.enabled), enabled);
    }

    public static Specification<Gun> byType(GunType type) {
        return (root, query, builder) ->
                builder.equal(root.get(Gun.Fields.type), type);
    }

    public static Specification<Gun> byGunIds(Iterable<Integer> gunIds) {
        return (root, query, builder) ->
                builder.in(root.get(Gun.Fields.id)).value(gunIds);
    }

    public static Specification<Gun> byUpdatedAfter(ZonedDateTime updatedAfter) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get(Gun.Fields.updatedTimestamp), updatedAfter);
    }
}