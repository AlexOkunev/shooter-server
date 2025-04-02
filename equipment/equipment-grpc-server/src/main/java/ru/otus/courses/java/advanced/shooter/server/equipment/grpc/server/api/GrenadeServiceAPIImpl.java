package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GrenadeService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;

@GRpcService
@RequiredArgsConstructor
public class GrenadeServiceAPIImpl extends GrenadeServiceAPIGrpc.GrenadeServiceAPIImplBase {
    private final GrenadeService grenadeService;

    @Override
    public void getGrenade(GetGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        responseObserver.onNext(grenadeService.getGrenadeInfo(request.getGrenadeId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledGrenade(GetEnabledGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        responseObserver.onNext(grenadeService.getEnabledGrenadeInfo(request.getGrenadeId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getGrenades(GetGrenadesRequest request, StreamObserver<GrenadeInfoListPage> responseObserver) {
        responseObserver.onNext(grenadeService.getGrenades(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createGrenade(CreateGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        responseObserver.onNext(grenadeService.createGrenade(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateGrenade(UpdateGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        responseObserver.onNext(grenadeService.updateGrenade(request));
        responseObserver.onCompleted();
    }
}
