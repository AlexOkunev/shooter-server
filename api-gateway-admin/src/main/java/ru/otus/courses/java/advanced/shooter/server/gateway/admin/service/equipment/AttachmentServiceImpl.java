package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.exception.AttachmentNotFoundException;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final ObjectFactory<AttachmentServiceAPIGrpc.AttachmentServiceAPIBlockingStub> attachmentServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<AttachmentInfo> getOne(int id) {
        GetEnabledAttachmentRequest request = GetEnabledAttachmentRequest.newBuilder()
                .setAttachmentId(id)
                .build();

        return Mono.fromCallable(() -> attachmentServiceAPIBlockingStubObjectFactory.getObject().getEnabledAttachment(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getEnabledAttachment start attachmentId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledAttachment success attachmentId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledAttachment error attachmentId={} err={}", id, e.getMessage(), e))
                .onErrorMap(StatusRuntimeException.class,
                        e -> e.getStatus().getCode() == Status.Code.NOT_FOUND ? new AttachmentNotFoundException(id) : e
                );
    }

    @Override
    public Mono<AttachmentInfoListPage> search(AttachmentsFilter requestFilter, PaginationRequest paginationRequest) {
        GetAttachmentsRequest request = GetAttachmentsRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ALL)
                .build();

        return Mono.fromCallable(() -> attachmentServiceAPIBlockingStubObjectFactory.getObject().getAttachments(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getAttachments start"))
                .doOnSuccess(resp -> log.info("gRPC getAttachments success items={}",
                        resp != null ? resp.getDataCount() : 0))
                .doOnError(e -> log.error("gRPC getAttachments error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<AttachmentInfoListPage> fetchByIds(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return Mono.just(AttachmentInfoListPage.newBuilder().build());
        }

        GetAttachmentsRequest request = GetAttachmentsRequest.newBuilder()
                .setFilter(AttachmentsFilter.newBuilder()
                        .addAllAttachmentIds(ids)
                        .build()
                )
                .setPaginationRequest(PaginationRequest.newBuilder()
                        .setPage(0)
                        .setCount(ids.size())
                        .build())
                .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ALL)
                .build();

        return Mono.fromCallable(() -> attachmentServiceAPIBlockingStubObjectFactory.getObject().getAttachments(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getAttachments start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getAttachments success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getAttachments error by ids={}: err={}", ids, e.getMessage(), e));
    }

    @Override
    public Mono<AttachmentInfo> create(AttachmentWritableData data) {
        CreateAttachmentRequest request = CreateAttachmentRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> attachmentServiceAPIBlockingStubObjectFactory.getObject().createAttachment(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC createAttachment start"))
                .doOnSuccess(resp -> log.info("gRPC createAttachment success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC createAttachment error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<AttachmentInfo> update(int id, AttachmentWritableData data) {
        UpdateAttachmentRequest request = UpdateAttachmentRequest.newBuilder()
                .setAttachmentId(id)
                .setData(data)
                .build();

        return Mono.fromCallable(() -> attachmentServiceAPIBlockingStubObjectFactory.getObject().updateAttachment(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC updateAttachment start"))
                .doOnSuccess(resp -> log.info("gRPC updateAttachment success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC updateAttachment error err={}", e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности