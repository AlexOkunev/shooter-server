package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;

public interface AttachmentService {

    Attachment getAttachment(int attachmentId);

    Attachment getEnabledAttachment(int attachmentId);

    Attachment createAttachment(@Valid @NotNull AttachmentSavedData data);

    Attachment updateAttachment(int attachmentId, @Valid @NotNull AttachmentSavedData data);

    Page<Attachment> getAttachments(
            @NotNull AttachmentFilterParams filterParams,
            @NotNull Pageable pageable,
            @NotNull RelatedEntitiesInclusionMode relatedEntitiesInclusionMode
    );
}