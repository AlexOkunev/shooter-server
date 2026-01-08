package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.AttachmentProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.RelatedEntitiesInclusionModeProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AttachmentService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;

@GRpcService
@RequiredArgsConstructor
public class AttachmentServiceAPIImpl extends AttachmentServiceAPIGrpc.AttachmentServiceAPIImplBase {
    private final AttachmentService attachmentService;
    private final AttachmentProtoMapper attachmentProtoMapper;
    private final PaginationInfoMapper paginationInfoMapper;
    private final RelatedEntitiesInclusionModeProtoMapper relatedEntitiesInclusionModeMapper;

    @Override
    public void getAttachment(GetAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        Attachment attachment = attachmentService.getAttachment(request.getAttachmentId());
        AttachmentInfo response = attachmentProtoMapper.toResponseWithGuns(attachment);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledAttachment(GetEnabledAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        Attachment attachment = attachmentService.getEnabledAttachment(request.getAttachmentId());
        AttachmentInfo response = attachmentProtoMapper.toResponseWithEnabledGuns(attachment);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAttachments(GetAttachmentsRequest request, StreamObserver<AttachmentInfoListPage> responseObserver) {
        AttachmentFilterParams filterParams = attachmentProtoMapper.toFilterParams(request);
        Pageable pageable = attachmentProtoMapper.toPageable(request);
        RelatedEntitiesInclusionMode mode = relatedEntitiesInclusionModeMapper.toBean(request.getCompatibleGunsInclusionMode());
        Page<Attachment> data = attachmentService.getAttachments(filterParams, pageable, mode);

        AttachmentInfoListPage response = AttachmentInfoListPage.newBuilder()
                .addAllData(attachmentProtoMapper.toResponseList(data.getContent(), request.getCompatibleGunsInclusionMode()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createAttachment(CreateAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        AttachmentSavedData data = attachmentProtoMapper.toSavedData(request);
        Attachment attachment = attachmentService.createAttachment(data);
        AttachmentInfo response = attachmentProtoMapper.toResponseWithGuns(attachment);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateAttachment(UpdateAttachmentRequest request, StreamObserver<AttachmentInfo> responseObserver) {
        AttachmentSavedData data = attachmentProtoMapper.toSavedData(request);
        Attachment attachment = attachmentService.updateAttachment(request.getAttachmentId(), data);
        AttachmentInfo response = attachmentProtoMapper.toResponseWithGuns(attachment);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}