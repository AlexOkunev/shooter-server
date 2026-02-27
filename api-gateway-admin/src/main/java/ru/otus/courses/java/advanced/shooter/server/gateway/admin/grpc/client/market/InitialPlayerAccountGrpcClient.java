package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import com.google.protobuf.Empty;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

public interface InitialPlayerAccountGrpcClient {

    InitialPlayerAccountItemsPage getInitialPlayerAccount(GetInitialPlayerAccountRequest request);

    Empty updateInitialPlayerAccount(UpdateInitialPlayerAccountRequest request);
}
