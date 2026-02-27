package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.IncrementallyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.AttachmentGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.AttachmentGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.AttachmentMapper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AttachmentDtoCacheService extends IncrementallyRefreshableCacheServiceImplBase<Integer, AttachmentDto> {

    private final AttachmentGrpcClient attachmentGrpcClient;
    private final AttachmentMapper attachmentMapper;

    public AttachmentDtoCacheService(
            @Value("${caches.attachment.page-size:100}") int dataPageSize,
            @Qualifier(AttachmentGrpcClientRateLimitingWrapper.NAME) AttachmentGrpcClient attachmentGrpcClient,
            AttachmentMapper attachmentMapper
    ) {
        super(new SoftReferenceMapCache<>(new ConcurrentHashMap<>()), dataPageSize);
        this.attachmentGrpcClient = attachmentGrpcClient;
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    protected CacheableDataPage<AttachmentDto> loadDataPage(int page, int size, ZonedDateTime lastRefreshTime) {
        log.info("Loading attachment data page: page={}, size={}, lastRefreshTime={}", page, size, lastRefreshTime);

        AttachmentInfoListPage attachments = attachmentGrpcClient.getAttachments(
                GetAttachmentsRequest.newBuilder()
                        .setFilter(AttachmentsFilter.newBuilder()
                                .setUpdatedAfter(lastRefreshTime.toInstant().toEpochMilli())
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(page)
                                .setCount(size)
                                .build())
                        .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ONLY_ENABLED)
                        .build()
        );

        log.info("Loaded attachment data page: page={}, size={}, total pages={}", page, attachments.getDataList().size(), attachments.getPaginationInfo().getTotalPages());

        attachments.getDataList()
                .forEach(attachmentInfo -> log.info("Loaded attachment {} {}", attachmentInfo.getId(), attachmentInfo.getName()));

        return new CacheableDataPage<>(
                attachmentMapper.toDtoList(attachments.getDataList()),
                attachments.getPaginationInfo().getCurrentPageNumber(),
                attachments.getPaginationInfo().getTotalPages()
        );
    }

    @Override
    protected Optional<AttachmentDto> produceDataById(Integer integer) {
        try {
            log.info("Loading attachment by ID: {}", integer);

            AttachmentInfo attachmentInfo = attachmentGrpcClient.getAttachment(
                    GetAttachmentRequest.newBuilder()
                            .setAttachmentId(integer)
                            .build()
            );

            log.info("Loaded attachment by ID: {}", integer);

            return Optional.of(attachmentMapper.toDto(attachmentInfo));
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                log.info("Attachment not found by ID: {}", integer);
                return Optional.empty();
            }

            throw e;
        }
    }

    @Override
    protected List<AttachmentDto> produceDataByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        log.info("Loading attachments by IDs: {}", ids);

        AttachmentInfoListPage attachments = attachmentGrpcClient.getAttachments(
                GetAttachmentsRequest.newBuilder()
                        .setFilter(AttachmentsFilter.newBuilder()
                                .addAllAttachmentIds(ids)
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(0)
                                .setCount(ids.size())
                                .build())
                        .build()
        );

        log.info("Loaded attachments by IDs: {}", ids);

        return attachmentMapper.toDtoList(attachments.getDataList());
    }
}
