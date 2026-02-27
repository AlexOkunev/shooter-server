package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;

import java.util.List;

@Slf4j
@Component(AmmunitionGrpcClientRateLimitingWrapper.NAME)
public class AmmunitionGrpcClientRateLimitingWrapper implements AmmunitionGrpcClient {

    public static final String NAME = "ammunitionGrpcClientRateLimitingWrapper";

    private final AmmunitionGrpcClient ammunitionGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public AmmunitionGrpcClientRateLimitingWrapper(
            @Qualifier(AmmunitionGrpcClientBaseImpl.NAME) AmmunitionGrpcClient ammunitionGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.ammunitionGrpcClient = ammunitionGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_equipment_rps"),
                rateLimiterRegistry.rateLimiter("grpc_equipment_rpm")
        );
    }

    @Override
    public AmmunitionInfo getAmmunition(GetAmmunitionRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> ammunitionGrpcClient.getAmmunition(request),
                rateLimiters
        );
    }

    @Override
    public AmmunitionInfo getEnabledAmmunition(GetEnabledAmmunitionRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> ammunitionGrpcClient.getEnabledAmmunition(request),
                rateLimiters
        );
    }

    @Override
    public AmmunitionInfoListPage getAmmunitionList(GetAmmunitionListRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> ammunitionGrpcClient.getAmmunitionList(request),
                rateLimiters
        );
    }

    @Override
    public AmmunitionInfo createAmmunition(CreateAmmunitionRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> ammunitionGrpcClient.createAmmunition(request),
                rateLimiters
        );
    }

    @Override
    public AmmunitionInfo updateAmmunition(UpdateAmmunitionRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> ammunitionGrpcClient.updateAmmunition(request),
                rateLimiters
        );
    }
}
