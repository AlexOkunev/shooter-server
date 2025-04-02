package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GunService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;

@GRpcService
@RequiredArgsConstructor
public class GunServiceAPIImpl extends GunServiceAPIGrpc.GunServiceAPIImplBase {
    private final GunService gunService;

    @Override
    public void getGun(GetGunRequest request, StreamObserver<GunInfo> responseObserver) {
        responseObserver.onNext(gunService.getGunInfo(request.getGunId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledGun(GetEnabledGunRequest request, StreamObserver<GunInfo> responseObserver) {
        responseObserver.onNext(gunService.getEnabledGunInfo(request.getGunId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getGuns(GetGunsRequest request, StreamObserver<GunInfoListPage> responseObserver) {
        responseObserver.onNext(gunService.getGuns(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createGun(CreateGunRequest request, StreamObserver<GunInfo> responseObserver) {
        responseObserver.onNext(gunService.createGun(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateGun(UpdateGunRequest request, StreamObserver<GunInfo> responseObserver) {
        responseObserver.onNext(gunService.updateGun(request));
        responseObserver.onCompleted();
    }
}
