package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.equipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.AttachmentGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.AttachmentGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;

import java.util.Collection;

@Slf4j
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentGrpcClient attachmentGrpcClient;
    private final Scheduler equipmentScheduler;

    public AttachmentServiceImpl(
            @Qualifier(AttachmentGrpcClientRateLimitingWrapper.NAME) AttachmentGrpcClient attachmentGrpcClient,
            @Qualifier(GrpcSchedulers.EQUIPMENT) Scheduler equipmentScheduler
    ) {
        this.attachmentGrpcClient = attachmentGrpcClient;
        this.equipmentScheduler = equipmentScheduler;
    }

    @Override
    public Mono<AttachmentInfo> getOne(int id) {
        GetEnabledAttachmentRequest request = GetEnabledAttachmentRequest.newBuilder()
                .setAttachmentId(id)
                .build();

        return Mono.fromCallable(() -> attachmentGrpcClient.getEnabledAttachment(request))
                .subscribeOn(equipmentScheduler)
                .doOnSubscribe(s -> log.info("gRPC getEnabledAttachment start attachmentId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledAttachment success attachmentId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledAttachment error attachmentId={} err={}", id, e.getMessage(), e));
    }

    @Override
    public Mono<AttachmentInfoListPage> search(AttachmentsFilter requestFilter, PaginationRequest paginationRequest) {
        GetAttachmentsRequest request = GetAttachmentsRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ONLY_ENABLED)
                .build();

        return Mono.fromCallable(() -> attachmentGrpcClient.getAttachments(request))
                .subscribeOn(equipmentScheduler)
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
                .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ONLY_ENABLED)
                .build();

        return Mono.fromCallable(() -> attachmentGrpcClient.getAttachments(request))
                .subscribeOn(equipmentScheduler)
                .doOnSubscribe(s -> log.info("gRPC getAttachments start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getAttachments success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getAttachments error by ids={}: err={}", ids, e.getMessage(), e));
    }
}