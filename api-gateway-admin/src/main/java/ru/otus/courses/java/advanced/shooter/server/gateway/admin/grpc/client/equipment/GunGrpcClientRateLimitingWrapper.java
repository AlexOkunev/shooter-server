package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.RateLimitingGrpcCallExecutor;

import java.util.List;

@Slf4j
@Component(GunGrpcClientRateLimitingWrapper.NAME)
public class GunGrpcClientRateLimitingWrapper implements GunGrpcClient {

    public static final String NAME = "gunGrpcClientRateLimitingWrapper";

    private final GunGrpcClient gunGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public GunGrpcClientRateLimitingWrapper(
            @Qualifier(GunGrpcClientBaseImpl.NAME) GunGrpcClient gunGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.gunGrpcClient = gunGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_equipment_rps"),
                rateLimiterRegistry.rateLimiter("grpc_equipment_rpm")
        );
    }

    @Override
    public GunInfo getGun(GetGunRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> gunGrpcClient.getGun(request),
                rateLimiters
        );
    }

    @Override
    public GunInfo getEnabledGun(GetEnabledGunRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> gunGrpcClient.getEnabledGun(request),
                rateLimiters
        );
    }

    @Override
    public GunInfoListPage getGuns(GetGunsRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> gunGrpcClient.getGuns(request),
                rateLimiters
        );
    }

    @Override
    public GunInfo createGun(CreateGunRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> gunGrpcClient.createGun(request),
                rateLimiters
        );
    }

    @Override
    public GunInfo updateGun(UpdateGunRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> gunGrpcClient.updateGun(request),
                rateLimiters
        );
    }
}
