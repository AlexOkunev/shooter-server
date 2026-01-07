package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.AmmunitionProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.RelatedEntitiesInclusionModeProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AmmunitionService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;

@GRpcService
@RequiredArgsConstructor
public class AmmunitionServiceAPIImpl extends AmmunitionServiceAPIGrpc.AmmunitionServiceAPIImplBase {
    private final AmmunitionService ammunitionService;
    private final AmmunitionProtoMapper ammunitionProtoMapper;
    private final PaginationInfoMapper paginationInfoMapper;
    private final RelatedEntitiesInclusionModeProtoMapper relatedEntitiesInclusionModeMapper;

    @Override
    public void getAmmunition(GetAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        Ammunition ammunition = ammunitionService.getAmmunition(request.getAmmunitionId());
        AmmunitionInfo response = ammunitionProtoMapper.toResponseWithGuns(ammunition);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledAmmunition(GetEnabledAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        Ammunition ammunition = ammunitionService.getEnabledAmmunition(request.getAmmunitionId());
        AmmunitionInfo response = ammunitionProtoMapper.toResponseWithEnabledGuns(ammunition);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAmmunitionList(GetAmmunitionListRequest request, StreamObserver<AmmunitionInfoListPage> responseObserver) {
        AmmunitionFilterParams filterParams = ammunitionProtoMapper.toFilterParams(request);
        Pageable pageable = ammunitionProtoMapper.toPageable(request);
        RelatedEntitiesInclusionMode mode = relatedEntitiesInclusionModeMapper.toBean(request.getCompatibleGunsInclusionMode());
        Page<Ammunition> data = ammunitionService.getAmmunitionList(filterParams, pageable, mode);

        AmmunitionInfoListPage response = AmmunitionInfoListPage.newBuilder()
                .addAllData(ammunitionProtoMapper.toResponseList(data.getContent(), request.getCompatibleGunsInclusionMode()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createAmmunition(CreateAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        AmmunitionSavedData data = ammunitionProtoMapper.toSavedData(request);
        Ammunition ammunition = ammunitionService.createAmmunition(data);
        AmmunitionInfo response = ammunitionProtoMapper.toResponseWithGuns(ammunition);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateAmmunition(UpdateAmmunitionRequest request, StreamObserver<AmmunitionInfo> responseObserver) {
        AmmunitionSavedData data = ammunitionProtoMapper.toSavedData(request);
        Ammunition ammunition = ammunitionService.updateAmmunition(request.getAmmunitionId(), data);
        AmmunitionInfo response = ammunitionProtoMapper.toResponseWithGuns(ammunition);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}