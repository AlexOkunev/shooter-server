package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;

public interface AttachmentService {

    AttachmentInfo getAttachmentInfo(int attachmentId);

    AttachmentInfo getEnabledAttachmentInfo(int attachmentId);

    AttachmentInfo createAttachment(CreateAttachmentRequest request);

    AttachmentInfo updateAttachment(UpdateAttachmentRequest request);

    AttachmentInfoListPage getAttachments(GetAttachmentsRequest request);
}