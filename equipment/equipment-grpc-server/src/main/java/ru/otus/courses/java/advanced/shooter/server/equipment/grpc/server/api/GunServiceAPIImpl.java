package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.GunProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.RelatedEntitiesInclusionModeProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GunService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;

@GRpcService
@RequiredArgsConstructor
public class GunServiceAPIImpl extends GunServiceAPIGrpc.GunServiceAPIImplBase {
    private final GunService gunService;
    private final GunProtoMapper gunProtoMapper;
    private final PaginationInfoMapper paginationInfoMapper;
    private final RelatedEntitiesInclusionModeProtoMapper relatedEntitiesInclusionModeMapper;

    @Override
    public void getGun(GetGunRequest request, StreamObserver<GunInfo> responseObserver) {
        Gun gun = gunService.getGun(request.getGunId());
        GunInfo response = gunProtoMapper.toResponseWithRelatedEntities(gun);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledGun(GetEnabledGunRequest request, StreamObserver<GunInfo> responseObserver) {
        Gun gun = gunService.getEnabledGun(request.getGunId());
        GunInfo response = gunProtoMapper.toResponseWithEnabledRelatedEntities(gun);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getGuns(GetGunsRequest request, StreamObserver<GunInfoListPage> responseObserver) {
        GunFilterParams filterParams = gunProtoMapper.toFilterParams(request);
        Pageable pageable = gunProtoMapper.toPageable(request);
        RelatedEntitiesInclusionMode mode = relatedEntitiesInclusionModeMapper.toBean(request.getRelatedEntitiesInclusionMode());
        Page<Gun> data = gunService.getGuns(filterParams, pageable, mode);

        GunInfoListPage response = GunInfoListPage.newBuilder()
                .addAllData(gunProtoMapper.toResponseList(data.getContent(), request.getRelatedEntitiesInclusionMode()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createGun(CreateGunRequest request, StreamObserver<GunInfo> responseObserver) {
        GunSavedData data = gunProtoMapper.toSavedData(request);
        Gun gun = gunService.createGun(data);
        GunInfo response = gunProtoMapper.toResponseWithRelatedEntities(gun);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateGun(UpdateGunRequest request, StreamObserver<GunInfo> responseObserver) {
        GunSavedData data = gunProtoMapper.toSavedData(request);
        Gun gun = gunService.updateGun(request.getGunId(), data);
        GunInfo response = gunProtoMapper.toResponseWithRelatedEntities(gun);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
