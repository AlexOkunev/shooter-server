package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;

@Slf4j
@Component(CurrencyGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class CurrencyGrpcClientBaseImpl implements CurrencyGrpcClient {

    public static final String NAME = "currencyGrpcClientBaseImpl";

    private final ObjectFactory<CurrencyServiceAPIGrpc.CurrencyServiceAPIBlockingStub> currencyServiceStubObjectFactory;

    @Override
    public CurrencyInfo getCurrency(GetCurrencyRequest request) {
        log.debug("Getting currency with request: {}", request);
        return currencyServiceStubObjectFactory.getObject().getCurrency(request);
    }

    @Override
    public CurrencyInfo getEnabledCurrency(GetEnabledCurrencyRequest request) {
        log.debug("Getting enabled currency with request: {}", request);
        return currencyServiceStubObjectFactory.getObject().getEnabledCurrency(request);
    }

    @Override
    public CurrencyInfoListPage getCurrencies(GetCurrenciesRequest request) {
        log.debug("Getting currencies with request: {}", request);
        return currencyServiceStubObjectFactory.getObject().getCurrencies(request);
    }

    @Override
    public CurrencyInfo createCurrency(CreateCurrencyRequest request) {
        log.debug("Creating currency with request: {}", request);
        return currencyServiceStubObjectFactory.getObject().createCurrency(request);
    }

    @Override
    public CurrencyInfo updateCurrency(UpdateCurrencyRequest request) {
        log.debug("Updating currency with request: {}", request);
        return currencyServiceStubObjectFactory.getObject().updateCurrency(request);
    }
}
