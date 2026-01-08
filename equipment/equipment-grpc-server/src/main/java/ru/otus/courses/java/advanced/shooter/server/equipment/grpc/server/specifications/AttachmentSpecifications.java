package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentType;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

import java.time.ZonedDateTime;

@UtilityClass
public class AttachmentSpecifications {
    public static Specification<Attachment> byNameStartsWith(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Attachment.Fields.name)), name.toLowerCase() + "%");
    }

    public static Specification<Attachment> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(Attachment.Fields.enabled), enabled);
    }

    public static Specification<Attachment> byCompatibleGunIds(Iterable<Integer> compatibleGunIds) {
        return (root, query, builder) ->
                builder.in(root.join(Attachment.Fields.compatibleGuns).get(Gun.Fields.id)).value(compatibleGunIds);
    }

    public static Specification<Attachment> byEnabledCompatibleGunIds(Iterable<Integer> compatibleGunIds) {
        return (root, query, builder) ->
                builder.and(
                        builder.in(root.join(Attachment.Fields.compatibleGuns).get(Gun.Fields.id)).value(compatibleGunIds),
                        builder.equal(root.join(Attachment.Fields.compatibleGuns).get(Gun.Fields.enabled), true)
                );
    }

    public static Specification<Attachment> byType(AttachmentType type) {
        return (root, query, builder) ->
                builder.equal(root.get(Attachment.Fields.type), type);
    }

    public static Specification<Attachment> byAttachmentIds(Iterable<Integer> attachmentIds) {
        return (root, query, builder) ->
                builder.in(root.get(Attachment.Fields.id)).value(attachmentIds);
    }

    public static Specification<Attachment> byUpdatedAfter(ZonedDateTime updatedAfter) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get(Attachment.Fields.updatedTimestamp), updatedAfter);
    }
}