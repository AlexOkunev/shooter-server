package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.InitialPlayerAccountFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.UpdateInitialPlayerAccountCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.InitialPlayerAccountProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.InitialPlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

@GRpcService
@RequiredArgsConstructor
public class InitialPlayerAccountServiceAPIImpl extends InitialPlayerAccountServiceAPIGrpc.InitialPlayerAccountServiceAPIImplBase {

    private final InitialPlayerAccountService initialPlayerAccountService;
    private final InitialPlayerAccountProtoMapper initialPlayerAccountProtoMapper;
    private final PaginationInfoMapper paginationInfoMapper;

    @Override
    public void getInitialPlayerAccount(GetInitialPlayerAccountRequest request, StreamObserver<InitialPlayerAccountItemsPage> responseObserver) {
        InitialPlayerAccountFilterParams filterParams = initialPlayerAccountProtoMapper.toFilterParams(request);
        Pageable pageable = initialPlayerAccountProtoMapper.toPageable(request);

        Page<InitialPlayerAccountItem> data = initialPlayerAccountService.getInitialPlayerAccountItems(filterParams, pageable);

        InitialPlayerAccountItemsPage response = InitialPlayerAccountItemsPage.newBuilder()
                .addAllData(initialPlayerAccountProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateInitialPlayerAccount(UpdateInitialPlayerAccountRequest request, StreamObserver<Empty> responseObserver) {
        UpdateInitialPlayerAccountCommand command = initialPlayerAccountProtoMapper.toCommand(request);
        initialPlayerAccountService.saveInitialPlayerAccount(command);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}

