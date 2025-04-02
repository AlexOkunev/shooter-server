package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AmmunitionService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;

@GRpcService
@RequiredArgsConstructor
public class AmmunitionServiceAPIImpl extends AmmunitionServiceAPIGrpc.AmmunitionServiceAPIImplBase {
    private final AmmunitionService ammunitionService;

    @Override
    public void getAmmunition(GetAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        responseObserver.onNext(ammunitionService.getAmmunitionInfo(request.getAmmunitionId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledAmmunition(GetEnabledAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        responseObserver.onNext(ammunitionService.getEnabledAmmunitionInfo(request.getAmmunitionId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getAmmunitionList(GetAmmunitionListRequest request, StreamObserver<AmmunitionInfoListPage> responseObserver) {
        responseObserver.onNext(ammunitionService.getAmmunitionList(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createAmmunition(CreateAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        responseObserver.onNext(ammunitionService.createAmmunition(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateAmmunition(UpdateAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        responseObserver.onNext(ammunitionService.updateAmmunition(request));
        responseObserver.onCompleted();
    }
}