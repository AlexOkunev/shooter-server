package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

public interface MoneyBundleGrpcClient {

    MoneyBundleInfo getMoneyBundle(GetMoneyBundleRequest request);

    MoneyBundleInfo getEnabledMoneyBundle(GetMoneyBundleRequest request);

    MoneyBundleInfoListPage getMoneyBundles(GetMoneyBundlesRequest request);

    MoneyBundleInfo createMoneyBundle(CreateMoneyBundleRequest request);

    MoneyBundleInfo updateMoneyBundle(UpdateMoneyBundleRequest request);
}
