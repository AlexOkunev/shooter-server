package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GrenadeGrpcClientBaseImpl;

import java.util.List;

@Slf4j
@Component(GrenadeGrpcClientRateLimitingWrapper.NAME)
public class GrenadeGrpcClientRateLimitingWrapper implements GrenadeGrpcClient {

    public static final String NAME = "grenadeGrpcClientRateLimitingWrapper";

    private final GrenadeGrpcClient grenadeGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public GrenadeGrpcClientRateLimitingWrapper(
            @Qualifier(GrenadeGrpcClientBaseImpl.NAME) GrenadeGrpcClient grenadeGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.grenadeGrpcClient = grenadeGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_equipment_rps"),
                rateLimiterRegistry.rateLimiter("grpc_equipment_rpm")
        );
    }

    @Override
    public GrenadeInfo getGrenade(GetGrenadeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> grenadeGrpcClient.getGrenade(request),
                rateLimiters
        );
    }

    @Override
    public GrenadeInfo getEnabledGrenade(GetEnabledGrenadeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> grenadeGrpcClient.getEnabledGrenade(request),
                rateLimiters
        );
    }

    @Override
    public GrenadeInfoListPage getGrenades(GetGrenadesRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> grenadeGrpcClient.getGrenades(request),
                rateLimiters
        );
    }

    @Override
    public GrenadeInfo createGrenade(CreateGrenadeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> grenadeGrpcClient.createGrenade(request),
                rateLimiters
        );
    }

    @Override
    public GrenadeInfo updateGrenade(UpdateGrenadeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> grenadeGrpcClient.updateGrenade(request),
                rateLimiters
        );
    }
}
