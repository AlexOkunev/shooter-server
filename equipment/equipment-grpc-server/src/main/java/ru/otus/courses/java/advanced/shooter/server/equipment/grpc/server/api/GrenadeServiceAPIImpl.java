package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.GrenadeProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GrenadeService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;

@GRpcService
@RequiredArgsConstructor
public class GrenadeServiceAPIImpl extends GrenadeServiceAPIGrpc.GrenadeServiceAPIImplBase {
    private final GrenadeService grenadeService;
    private final GrenadeProtoMapper grenadeProtoMapper;
    private final PaginationInfoMapper paginationInfoMapper;

    @Override
    public void getGrenade(GetGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        Grenade grenade = grenadeService.getGrenade(request.getGrenadeId());
        GrenadeInfo response = grenadeProtoMapper.toResponse(grenade);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledGrenade(GetEnabledGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        Grenade grenade = grenadeService.getEnabledGrenade(request.getGrenadeId());
        GrenadeInfo response = grenadeProtoMapper.toResponse(grenade);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getGrenades(GetGrenadesRequest request, StreamObserver<GrenadeInfoListPage> responseObserver) {
        GrenadeFilterParams filterParams = grenadeProtoMapper.toFilterParams(request);
        Pageable pageable = grenadeProtoMapper.toPageable(request);
        Page<Grenade> data = grenadeService.getGrenades(filterParams, pageable);

        GrenadeInfoListPage response = GrenadeInfoListPage.newBuilder()
                .addAllData(grenadeProtoMapper.toResponseList(data))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createGrenade(CreateGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        GrenadeSavedData data = grenadeProtoMapper.toSavedData(request);
        Grenade grenade = grenadeService.createGrenade(data);
        GrenadeInfo response = grenadeProtoMapper.toResponse(grenade);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateGrenade(UpdateGrenadeRequest request, StreamObserver<GrenadeInfo> responseObserver) {
        GrenadeSavedData data = grenadeProtoMapper.toSavedData(request);
        Grenade grenade = grenadeService.updateGrenade(request.getGrenadeId(), data);
        GrenadeInfo response = grenadeProtoMapper.toResponse(grenade);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
