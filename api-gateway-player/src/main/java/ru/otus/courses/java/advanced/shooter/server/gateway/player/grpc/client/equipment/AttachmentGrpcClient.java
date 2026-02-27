package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;


import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;

public interface AttachmentGrpcClient {

    AttachmentInfo getAttachment(GetAttachmentRequest request);

    AttachmentInfo getEnabledAttachment(GetEnabledAttachmentRequest request);

    AttachmentInfoListPage getAttachments(GetAttachmentsRequest request);

    AttachmentInfo createAttachment(CreateAttachmentRequest request);

    AttachmentInfo updateAttachment(UpdateAttachmentRequest request);
}
