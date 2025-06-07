package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

public interface MoneyBundleService {
    MoneyBundleInfo getMoneyBundle(GetMoneyBundleRequest request);

    MoneyBundleInfoListPage getMoneyBundles(GetMoneyBundlesRequest request);

    MoneyBundleInfo createMoneyBundle(CreateMoneyBundleRequest request);

    MoneyBundleInfo updateMoneyBundle(UpdateMoneyBundleRequest request);

    MoneyBundleInfo getEnabledMoneyBundle(GetMoneyBundleRequest request);
}
