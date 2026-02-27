package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

@Slf4j
@Component(MoneyBundleGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class MoneyBundleGrpcClientBaseImpl implements MoneyBundleGrpcClient {

    public static final String NAME = "moneyBundleGrpcClientBaseImpl";

    private final ObjectFactory<MoneyBundleServiceAPIGrpc.MoneyBundleServiceAPIBlockingStub>
            moneyBundleServiceAPIBlockingStubObjectFactory;

    @Override
    public MoneyBundleInfo getMoneyBundle(GetMoneyBundleRequest request) {
        log.debug("Getting money bundle with request: {}", request);
        return moneyBundleServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundle(request);
    }

    @Override
    public MoneyBundleInfo getEnabledMoneyBundle(GetMoneyBundleRequest request) {
        log.debug("Getting enabled money bundle with request: {}", request);
        return moneyBundleServiceAPIBlockingStubObjectFactory.getObject().getEnabledMoneyBundle(request);
    }

    @Override
    public MoneyBundleInfoListPage getMoneyBundles(GetMoneyBundlesRequest request) {
        log.debug("Getting money bundles with request: {}", request);
        return moneyBundleServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundles(request);
    }

    @Override
    public MoneyBundleInfo createMoneyBundle(CreateMoneyBundleRequest request) {
        log.debug("Creating money bundle with request: {}", request);
        return moneyBundleServiceAPIBlockingStubObjectFactory.getObject().createMoneyBundle(request);
    }

    @Override
    public MoneyBundleInfo updateMoneyBundle(UpdateMoneyBundleRequest request) {
        log.debug("Updating money bundle with request: {}", request);
        return moneyBundleServiceAPIBlockingStubObjectFactory.getObject().updateMoneyBundle(request);
    }
}
