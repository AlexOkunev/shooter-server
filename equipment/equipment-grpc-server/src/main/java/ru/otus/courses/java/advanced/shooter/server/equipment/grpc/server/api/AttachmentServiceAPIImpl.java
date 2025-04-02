package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AttachmentService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;

@GRpcService
@RequiredArgsConstructor
public class AttachmentServiceAPIImpl extends AttachmentServiceAPIGrpc.AttachmentServiceAPIImplBase {
    private final AttachmentService attachmentService;

    @Override
    public void getAttachment(GetAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        responseObserver.onNext(attachmentService.getAttachmentInfo(request.getAttachmentId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledAttachment(GetEnabledAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        responseObserver.onNext(attachmentService.getEnabledAttachmentInfo(request.getAttachmentId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getAttachments(GetAttachmentsRequest request, StreamObserver<AttachmentInfoListPage> responseObserver) {
        responseObserver.onNext(attachmentService.getAttachments(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createAttachment(CreateAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        responseObserver.onNext(attachmentService.createAttachment(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateAttachment(UpdateAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        responseObserver.onNext(attachmentService.updateAttachment(request));
        responseObserver.onCompleted();
    }
}