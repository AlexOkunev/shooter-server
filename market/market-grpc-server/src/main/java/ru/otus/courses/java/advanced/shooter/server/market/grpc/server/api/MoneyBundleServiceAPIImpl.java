package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.MoneyBundleProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

@GRpcService
@RequiredArgsConstructor
public class MoneyBundleServiceAPIImpl extends MoneyBundleServiceAPIGrpc.MoneyBundleServiceAPIImplBase {

    private final MoneyBundleService moneyBundleService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final MoneyBundleProtoMapper moneyBundleProtoMapper;

    @Override
    public void getMoneyBundle(GetMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        MoneyBundle moneyBundle = moneyBundleService.getMoneyBundle(request.getId());
        MoneyBundleInfo response = moneyBundleProtoMapper.toResponse(moneyBundle);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledMoneyBundle(GetMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        MoneyBundle moneyBundle = moneyBundleService.getEnabledMoneyBundle(request.getId());
        MoneyBundleInfo response = moneyBundleProtoMapper.toResponse(moneyBundle);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMoneyBundles(GetMoneyBundlesRequest request, StreamObserver<MoneyBundleInfoListPage> responseObserver) {
        MoneyBundleFilterParams filterParams = moneyBundleProtoMapper.toFilterParams(request);
        Pageable pageable = moneyBundleProtoMapper.toPageable(request);

        Page<MoneyBundle> data = moneyBundleService.getMoneyBundles(filterParams, pageable);

        MoneyBundleInfoListPage response = MoneyBundleInfoListPage.newBuilder()
                .addAllData(moneyBundleProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createMoneyBundle(CreateMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        MoneyBundleSavedData data = moneyBundleProtoMapper.toSavedData(request);
        MoneyBundle moneyBundle = moneyBundleService.createMoneyBundle(data);
        MoneyBundleInfo response = moneyBundleProtoMapper.toResponse(moneyBundle);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateMoneyBundle(UpdateMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        MoneyBundleSavedData data = moneyBundleProtoMapper.toSavedData(request);
        MoneyBundle moneyBundle = moneyBundleService.updateMoneyBundle(request.getId(), request.getVersion(), data);
        MoneyBundleInfo response = moneyBundleProtoMapper.toResponse(moneyBundle);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
