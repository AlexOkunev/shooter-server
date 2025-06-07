package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.ModifyInitialPlayerAccountRequest;

public interface InitialPlayerAccountService {
    InitialPlayerAccountItemsPage getInitialPlayerInventory(GetInitialPlayerAccountRequest request);

    void modifyInitialPlayerAccount(ModifyInitialPlayerAccountRequest request);
}
