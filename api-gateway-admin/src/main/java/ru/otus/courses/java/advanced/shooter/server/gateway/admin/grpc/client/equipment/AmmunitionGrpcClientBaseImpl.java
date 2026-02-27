package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;

@Slf4j
@Component(AmmunitionGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class AmmunitionGrpcClientBaseImpl implements AmmunitionGrpcClient {

    public static final String NAME = "ammunitionGrpcClientBaseImpl";

    private final ObjectFactory<AmmunitionServiceAPIGrpc.AmmunitionServiceAPIBlockingStub> ammunitionServiceAPIBlockingStubObjectFactory;

    @Override
    public AmmunitionInfo getAmmunition(GetAmmunitionRequest request) {
        log.debug("Getting ammunition with request: {}", request);
        return ammunitionServiceAPIBlockingStubObjectFactory.getObject().getAmmunition(request);
    }

    @Override
    public AmmunitionInfo getEnabledAmmunition(GetEnabledAmmunitionRequest request) {
        log.debug("Getting enabled ammunition with request: {}", request);
        return ammunitionServiceAPIBlockingStubObjectFactory.getObject().getEnabledAmmunition(request);
    }

    @Override
    public AmmunitionInfoListPage getAmmunitionList(GetAmmunitionListRequest request) {
        log.debug("Getting ammunition list with request: {}", request);
        return ammunitionServiceAPIBlockingStubObjectFactory.getObject().getAmmunitionList(request);
    }

    @Override
    public AmmunitionInfo createAmmunition(CreateAmmunitionRequest request) {
        log.debug("Creating ammunition with request: {}", request);
        return ammunitionServiceAPIBlockingStubObjectFactory.getObject().createAmmunition(request);
    }

    @Override
    public AmmunitionInfo updateAmmunition(UpdateAmmunitionRequest request) {
        log.debug("Updating ammunition with request: {}", request);
        return ammunitionServiceAPIBlockingStubObjectFactory.getObject().updateAmmunition(request);
    }
}
