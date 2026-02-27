package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;

@Slf4j
@Component(GrenadeGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class GrenadeGrpcClientBaseImpl implements GrenadeGrpcClient {

    public static final String NAME = "grenadeGrpcClientBaseImpl";

    private final ObjectFactory<GrenadeServiceAPIGrpc.GrenadeServiceAPIBlockingStub> grenadeServiceAPIBlockingStubObjectFactory;

    @Override
    public GrenadeInfo getGrenade(GetGrenadeRequest request) {
        log.debug("Getting grenade with request: {}", request);
        return grenadeServiceAPIBlockingStubObjectFactory.getObject().getGrenade(request);
    }

    @Override
    public GrenadeInfo getEnabledGrenade(GetEnabledGrenadeRequest request) {
        log.debug("Getting enabled grenade with request: {}", request);
        return grenadeServiceAPIBlockingStubObjectFactory.getObject().getEnabledGrenade(request);
    }

    @Override
    public GrenadeInfoListPage getGrenades(GetGrenadesRequest request) {
        log.debug("Getting grenades with request: {}", request);
        return grenadeServiceAPIBlockingStubObjectFactory.getObject().getGrenades(request);
    }

    @Override
    public GrenadeInfo createGrenade(CreateGrenadeRequest request) {
        log.debug("Creating grenade with request: {}", request);
        return grenadeServiceAPIBlockingStubObjectFactory.getObject().createGrenade(request);
    }

    @Override
    public GrenadeInfo updateGrenade(UpdateGrenadeRequest request) {
        log.debug("Updating grenade with request: {}", request);
        return grenadeServiceAPIBlockingStubObjectFactory.getObject().updateGrenade(request);
    }
}
