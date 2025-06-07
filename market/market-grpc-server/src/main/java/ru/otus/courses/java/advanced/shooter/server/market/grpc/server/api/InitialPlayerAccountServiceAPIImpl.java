package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.InitialPlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.ModifyInitialPlayerAccountRequest;

@GRpcService
@RequiredArgsConstructor
public class InitialPlayerAccountServiceAPIImpl extends InitialPlayerAccountServiceAPIGrpc.InitialPlayerAccountServiceAPIImplBase {
    private final InitialPlayerAccountService initialPlayerAccountService;

    @Override
    public void getInitialPlayerAccount(GetInitialPlayerAccountRequest request, StreamObserver<InitialPlayerAccountItemsPage> responseObserver) {
        responseObserver.onNext(initialPlayerAccountService.getInitialPlayerInventory(request));
        responseObserver.onCompleted();
    }

    @Override
    public void modifyInitialPlayerAccount(ModifyInitialPlayerAccountRequest request, StreamObserver<Empty> responseObserver) {
        initialPlayerAccountService.modifyInitialPlayerAccount(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}

