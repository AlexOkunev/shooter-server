package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;

import java.util.List;

@Slf4j
@Component(AttachmentGrpcClientRateLimitingWrapper.NAME)
public class AttachmentGrpcClientRateLimitingWrapper implements AttachmentGrpcClient {

    public static final String NAME = "attachmentGrpcClientRateLimitingWrapper";

    private final AttachmentGrpcClient attachmentGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public AttachmentGrpcClientRateLimitingWrapper(
            @Qualifier(AttachmentGrpcClientBaseImpl.NAME) AttachmentGrpcClient attachmentGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.attachmentGrpcClient = attachmentGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_equipment_rps"),
                rateLimiterRegistry.rateLimiter("grpc_equipment_rpm")
        );
    }

    @Override
    public AttachmentInfo getAttachment(GetAttachmentRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> attachmentGrpcClient.getAttachment(request),
                rateLimiters
        );
    }

    @Override
    public AttachmentInfo getEnabledAttachment(GetEnabledAttachmentRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> attachmentGrpcClient.getEnabledAttachment(request),
                rateLimiters
        );
    }

    @Override
    public AttachmentInfoListPage getAttachments(GetAttachmentsRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> attachmentGrpcClient.getAttachments(request),
                rateLimiters
        );
    }

    @Override
    public AttachmentInfo createAttachment(CreateAttachmentRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> attachmentGrpcClient.createAttachment(request),
                rateLimiters
        );
    }

    @Override
    public AttachmentInfo updateAttachment(UpdateAttachmentRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> attachmentGrpcClient.updateAttachment(request),
                rateLimiters
        );
    }
}
