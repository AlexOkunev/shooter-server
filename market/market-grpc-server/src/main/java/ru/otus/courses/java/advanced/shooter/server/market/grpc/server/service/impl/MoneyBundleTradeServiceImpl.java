package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.MoneyBundleCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.*;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.PaymentStubRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.MoneyBundleTradeMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleTradeRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.MoneyBundleTradeSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.util.TransactionExecutor;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.PaymentServiceAPIGrpc;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class MoneyBundleTradeServiceImpl implements MoneyBundleTradeService {

    private final MoneyBundleCacheService moneyBundleCacheService;
    private final MoneyBundleTradeRepository moneyBundleTradeRepository;
    private final PlayerAccountRepository playerAccountRepository;
    private final MoneyBundleTradeMapper moneyBundleTradeMapper;
    private final PaymentStubRequestMapper paymentStubRequestMapper;
    private final TransactionExecutor transactionExecutor;
    private final ObjectFactory<PaymentServiceAPIGrpc.PaymentServiceAPIBlockingStub> paymentServiceAPIBlockingStub;

    @Override
    public MoneyBundleTrade createMoneyBundleTrade(@NotNull UUID playerUuid, int moneyBundleId) {
        MoneyBundle moneyBundle = moneyBundleCacheService.getById(moneyBundleId)
                .filter(MoneyBundle::isEnabled)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id %d not found".formatted(moneyBundleId)));

        ReferenceCurrency currency = moneyBundle.getCurrency();

        if (!currency.isEnabled()) {
            throw new InvalidRequestException("Currency is not enabled");
        }

        if (!currency.isCanBeBought()) {
            throw new InvalidRequestException("Currency can not be traded");
        }

        PlayerAccount playerAccount = playerAccountRepository.findByPlayerUuid(playerUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Player with uuid %s not found".formatted(playerUuid)));

        MoneyBundleTrade moneyBundleTrade = moneyBundleTradeMapper.toEntity(moneyBundle, playerAccount.getPlayerUuid());

        return moneyBundleTradeRepository.save(moneyBundleTrade);
    }

    @Override
    public MoneyBundleTrade getMoneyBundleTrade(@NotNull UUID playerUuid, @NotNull UUID tradeUuid) {
        return moneyBundleTradeRepository.findByPlayerUuidAndUuid(playerUuid, tradeUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle trade with id '%s' and player id '%s' not found"
                        .formatted(playerUuid, tradeUuid)));
    }

    @Override
    public Page<MoneyBundleTrade> getMoneyBundleTrades(@NotNull @Valid MoneyBundleTradeFilterParams filterParams,
                                                       @NotNull Pageable pageable) {
        Specification<MoneyBundleTrade> specification = getSpecification(filterParams);
        return moneyBundleTradeRepository.findAll(specification, pageable);
    }

    //TODO обернуть все в одну транзакцию и посмотреть что будет под высокой нагрузкой
    @Override
    public MoneyBundleTrade performMoneyBundleTradePayment(@NotNull UUID playerUuid, @NotNull UUID tradeUuid) {
        MoneyBundleTrade moneyBundleTrade = moneyBundleTradeRepository.findByPlayerUuidAndUuid(playerUuid, tradeUuid)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Money bundle trade with id '%s' and player id '%s' not found".formatted(playerUuid, tradeUuid)));

        //TODO!!! потом в job сбрасывать до или FAILED. добавить todo что статусы подвисших сначала запросить через grpc. аналогично в product trade
        int modified = moneyBundleTradeRepository.updateStatusChecked(
                playerUuid,
                tradeUuid,
                MoneyBundleTradeStatus.CREATED,
                MoneyBundleTradeStatus.PAYMENT_WAIT,
                ZonedDateTime.now(ZoneOffset.UTC)
        );

        if (modified == 0) {
            throw new InvalidRequestException("Money bundle trade has incorrect status");
        }

        PlayerAccount playerAccount = playerAccountRepository.findByPlayerUuid(playerUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Player account not found"));

        if (StringUtils.isBlank(playerAccount.getEmail())) {
            throw new InvalidRequestException("Player account email is not present");
        }

        CreatePaymentRequest paymentRequest = paymentStubRequestMapper.toRequest(
                moneyBundleTrade,
                playerAccount.getEmail()
        );

        CreatePaymentResponse paymentResponse = paymentServiceAPIBlockingStub.getObject().createPayment(paymentRequest);

        MoneyBundleTradePayment moneyBundleTradePayment = paymentStubRequestMapper.toPayment(paymentResponse);

        return transactionExecutor.execute(() -> {
            MoneyBundleTrade tempTrade = moneyBundleTradeRepository.findByPlayerUuidAndUuid(playerUuid, tradeUuid)
                    .orElseThrow(() -> new ObjectNotFoundException("Trade not found"));

            if (tempTrade.getStatus() != MoneyBundleTradeStatus.PAYMENT_WAIT) {
                throw new RuntimeException("Trade is not in payment wait status");
            }

            tempTrade.setPayment(moneyBundleTradePayment);
            tempTrade.setStatus(MoneyBundleTradeStatus.PAYMENT_PENDING);

            return moneyBundleTradeRepository.save(tempTrade);
        });
    }

    private Specification<MoneyBundleTrade> getSpecification(MoneyBundleTradeFilterParams filter) {
        List<Specification<MoneyBundleTrade>> specifications = new ArrayList<>();

        specifications.add(MoneyBundleTradeSpecifications.byPlayerUuid(filter.getPlayerUuid()));

        if (!filter.getCurrencyIds().isEmpty()) {
            specifications.add(MoneyBundleTradeSpecifications.byCurrencyIds(filter.getCurrencyIds()));
        }

        if (!filter.getStatuses().isEmpty()) {
            specifications.add(MoneyBundleTradeSpecifications.byStatuses(filter.getStatuses()));
        }

        return Specification.allOf(specifications);
    }
}

//TODO везде установить transactional readonly где нужно