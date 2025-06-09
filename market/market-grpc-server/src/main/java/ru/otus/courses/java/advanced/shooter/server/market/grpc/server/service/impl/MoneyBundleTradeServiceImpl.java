package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.MoneyBundleCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTradePayment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccount;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.MoneyBundleTradeMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.MoneyBundleTradeStatusMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PaymentStubRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleTradeRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.MoneyBundleTradeSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.*;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.courses.java.advanced.shooter.server.payment.protobuf.PaymentServiceAPIGrpc.PaymentServiceAPIBlockingStub;

@Service
@RequiredArgsConstructor
public class MoneyBundleTradeServiceImpl implements MoneyBundleTradeService {

    private final MoneyBundleCacheService moneyBundleCacheService;

    private final PaginationInfoMapper paginationInfoMapper;

    private final MoneyBundleTradeRepository moneyBundleTradeRepository;

    private final PlayerAccountRepository playerAccountRepository;

    private final MoneyBundleTradeMapper moneyBundleTradeMapper;

    private final MoneyBundleTradeStatusMapper moneyBundleTradeStatusMapper;

    private final PaymentStubRequestMapper paymentStubRequestMapper;

    private final ObjectFactory<PaymentServiceAPIBlockingStub> paymentServiceAPIBlockingStub;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, MoneyBundleTrade.Fields.id);

    @Override
    public MoneyBundleTradeInfo createMoneyBundleTrade(CreateMoneyBundleTradeRequest request) {
        MoneyBundle moneyBundle = moneyBundleCacheService.getById(request.getMoneyBundleId())
                .filter(MoneyBundle::isEnabled)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id %d not found".formatted(request.getMoneyBundleId())));

        if (!moneyBundle.getCurrency().isEnabled()) {
            throw new InvalidRequestException("Currency is not enabled");
        }

        if (!moneyBundle.getCurrency().isCanBeTraded()) {
            throw new InvalidRequestException("Currency can not be traded");
        }

        PlayerAccount playerAccount = playerAccountRepository.findByPlayerId(request.getPlayerId())
                .orElseThrow(() -> new ObjectNotFoundException("Player with id %d not found".formatted(request.getPlayerId())));

        MoneyBundleTrade moneyBundleTrade = moneyBundleTradeMapper.toEntity(playerAccount.getPlayerId(), moneyBundle);
        moneyBundleTrade = moneyBundleTradeRepository.save(moneyBundleTrade);

        return moneyBundleTradeMapper.toResponse(moneyBundleTrade);
    }

    @Override
    public MoneyBundleTradeInfo getMoneyBundleTrade(GetMoneyBundleTradeRequest request) {
        return moneyBundleTradeRepository.findByIdAndPlayerId(request.getTradeId(), request.getPlayerId())
                .map(moneyBundleTradeMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle trade with id '%d' and player id '%d' not found".formatted(request.getTradeId(), request.getPlayerId())));
    }

    @Override
    public MoneyBundleTradeInfoListPage getMoneyBundleTrades(GetMoneyBundleTradesRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<MoneyBundleTrade> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Page<MoneyBundleTrade> data = moneyBundleTradeRepository.findAll(specification, pageable);

        return MoneyBundleTradeInfoListPage.newBuilder()
                .addAllData(data.map(moneyBundleTradeMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    @Override
    public MoneyBundleTradeInfo makeMoneyBundleTradePayment(MakeMoneyBundleTradePaymentRequest request) {
        MoneyBundleTrade trade = moneyBundleTradeRepository.findByIdAndPlayerId(request.getTradeId(), request.getPlayerId())
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle trade with id '%d' and player id '%d' not found"
                        .formatted(request.getTradeId(), request.getPlayerId())));

        if (trade.getStatus() != MoneyBundleTradeStatus.CREATED) {
            throw new InvalidRequestException("Money bundle trade with id '%d' has incorrect status".formatted(request.getTradeId()));
        }

        CreatePaymentRequest paymentRequest = paymentStubRequestMapper.toRequest(trade, request.getPlayerEmail());
        CreatePaymentResponse paymentResponse = paymentServiceAPIBlockingStub.getObject().createPayment(paymentRequest);

        MoneyBundleTradePayment moneyBundleTradePayment = paymentStubRequestMapper.toPayment(paymentResponse);

        trade.setPayment(moneyBundleTradePayment);
        trade.setStatus(MoneyBundleTradeStatus.PAYMENT_PENDING);
        trade = moneyBundleTradeRepository.save(trade);

        return moneyBundleTradeMapper.toResponse(trade);
    }

    private Specification<MoneyBundleTrade> getSpecification(GetMoneyBundleTradesRequest.Filter filter) {
        List<Specification<MoneyBundleTrade>> specifications = new ArrayList<>();

        if (filter.hasPlayerId()) {
            specifications.add(MoneyBundleTradeSpecifications.byPlayerId(filter.getPlayerId()));
        }

        if (filter.getTradeIdsCount() > 0) {
            specifications.add(MoneyBundleTradeSpecifications.byIds(filter.getTradeIdsList()));
        }

        if (filter.getMoneyBundleIdsCount() > 0) {
            specifications.add(MoneyBundleTradeSpecifications.byMoneyBundleIds(filter.getMoneyBundleIdsList()));
        }

        if (filter.getCurrencyIdsCount() > 0) {
            specifications.add(MoneyBundleTradeSpecifications.byCurrencyIds(filter.getCurrencyIdsList()));
        }

        if (filter.getStatusesCount() > 0) {
            List<MoneyBundleTradeStatus> statues = moneyBundleTradeStatusMapper.toEntityList(filter.getStatusesList());
            specifications.add(MoneyBundleTradeSpecifications.byStatuses(statues));
        }

        return Specification.allOf(specifications);
    }
}