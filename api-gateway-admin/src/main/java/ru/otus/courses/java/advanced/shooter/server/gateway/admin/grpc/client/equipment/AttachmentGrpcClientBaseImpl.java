package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;

@Slf4j
@Component(AttachmentGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class AttachmentGrpcClientBaseImpl implements AttachmentGrpcClient {

    public static final String NAME = "attachmentGrpcClientBaseImpl";

    private final ObjectFactory<AttachmentServiceAPIGrpc.AttachmentServiceAPIBlockingStub> attachmentServiceAPIBlockingStubObjectFactory;

    @Override
    public AttachmentInfo getAttachment(GetAttachmentRequest request) {
        log.debug("Getting attachment with request: {}", request);
        return attachmentServiceAPIBlockingStubObjectFactory.getObject().getAttachment(request);
    }

    @Override
    public AttachmentInfo getEnabledAttachment(GetEnabledAttachmentRequest request) {
        log.debug("Getting enabled attachment with request: {}", request);
        return attachmentServiceAPIBlockingStubObjectFactory.getObject().getEnabledAttachment(request);
    }

    @Override
    public AttachmentInfoListPage getAttachments(GetAttachmentsRequest request) {
        log.debug("Getting attachments with request: {}", request);
        return attachmentServiceAPIBlockingStubObjectFactory.getObject().getAttachments(request);
    }

    @Override
    public AttachmentInfo createAttachment(CreateAttachmentRequest request) {
        log.debug("Creating attachment with request: {}", request);
        return attachmentServiceAPIBlockingStubObjectFactory.getObject().createAttachment(request);
    }

    @Override
    public AttachmentInfo updateAttachment(UpdateAttachmentRequest request) {
        log.debug("Updating attachment with request: {}", request);
        return attachmentServiceAPIBlockingStubObjectFactory.getObject().updateAttachment(request);
    }
}
