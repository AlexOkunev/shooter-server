package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GunGrpcClient;

@Slf4j
@Component(GunGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class GunGrpcClientBaseImpl implements GunGrpcClient {

    public static final String NAME = "gunGrpcClientBaseImpl";

    private final ObjectFactory<GunServiceAPIGrpc.GunServiceAPIBlockingStub> gunServiceAPIBlockingStubObjectFactory;

    @Override
    public GunInfo getGun(GetGunRequest request) {
        log.debug("Getting gun with request: {}", request);
        return gunServiceAPIBlockingStubObjectFactory.getObject().getGun(request);
    }

    @Override
    public GunInfo getEnabledGun(GetEnabledGunRequest request) {
        log.debug("Getting enabled gun with request: {}", request);
        return gunServiceAPIBlockingStubObjectFactory.getObject().getEnabledGun(request);
    }

    @Override
    public GunInfoListPage getGuns(GetGunsRequest request) {
        log.debug("Getting guns with request: {}", request);
        return gunServiceAPIBlockingStubObjectFactory.getObject().getGuns(request);
    }

    @Override
    public GunInfo createGun(CreateGunRequest request) {
        log.debug("Creating gun with request: {}", request);
        return gunServiceAPIBlockingStubObjectFactory.getObject().createGun(request);
    }

    @Override
    public GunInfo updateGun(UpdateGunRequest request) {
        log.debug("Updating gun with request: {}", request);
        return gunServiceAPIBlockingStubObjectFactory.getObject().updateGun(request);
    }
}
