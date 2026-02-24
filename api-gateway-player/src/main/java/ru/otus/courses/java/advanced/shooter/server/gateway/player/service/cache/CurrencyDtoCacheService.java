package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.IncrementallyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.CurrencyMapper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CurrencyDtoCacheService extends IncrementallyRefreshableCacheServiceImplBase<Integer, CurrencyDto> {

    private final ObjectFactory<CurrencyServiceAPIGrpc.CurrencyServiceAPIBlockingStub> currencyServiceStubObjectFactory;
    private final CurrencyMapper currencyMapper;

    public CurrencyDtoCacheService(
            @Value("${caches.currency.page-size:100}") int dataPageSize,
            ObjectFactory<CurrencyServiceAPIGrpc.CurrencyServiceAPIBlockingStub> currencyServiceStubObjectFactory,
            CurrencyMapper currencyMapper
    ) {
        super(new SoftReferenceMapCache<>(new ConcurrentHashMap<>()), dataPageSize);
        this.currencyServiceStubObjectFactory = currencyServiceStubObjectFactory;
        this.currencyMapper = currencyMapper;
    }

    @Override
    protected CacheableDataPage<CurrencyDto> loadDataPage(int page, int size, ZonedDateTime lastRefreshTime) {
        log.info("Loading currency data page: page={}, size={}, lastRefreshTime={}", page, size, lastRefreshTime);

        CurrencyInfoListPage currencies = currencyServiceStubObjectFactory.getObject().getCurrencies(
                GetCurrenciesRequest.newBuilder()
                        .setFilter(CurrenciesFilter.newBuilder()
                                .setUpdatedAfter(lastRefreshTime.toInstant().toEpochMilli())
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(page)
                                .setCount(size)
                                .build())
                        .build()
        );

        log.info("Loaded currency data page: page={}, size={}, total pages={}", page, currencies.getDataList().size(), currencies.getPaginationInfo().getTotalPages());

        currencies.getDataList()
                .forEach(currencyInfo -> log.info("Loaded currency {} {}", currencyInfo.getId(), currencyInfo.getName()));

        return new CacheableDataPage<>(
                currencyMapper.toDtoList(currencies.getDataList()),
                currencies.getPaginationInfo().getCurrentPageNumber(),
                currencies.getPaginationInfo().getTotalPages()
        );
    }

    @Override
    protected Optional<CurrencyDto> produceDataById(Integer integer) {
        try {
            log.info("Loading currency by ID: {}", integer);

            CurrencyInfo currencyInfo = currencyServiceStubObjectFactory.getObject().getCurrency(
                    GetCurrencyRequest.newBuilder()
                            .setCurrencyId(integer)
                            .build()
            );

            log.info("Loaded currency by ID: {}", integer);

            return Optional.of(currencyMapper.toDto(currencyInfo));
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                log.info("Currency not found by ID: {}", integer);
                return Optional.empty();
            }

            throw e;
        }
    }

    @Override
    protected List<CurrencyDto> produceDataByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        log.info("Loading currencies by IDs: {}", ids);

        CurrencyInfoListPage currencies = currencyServiceStubObjectFactory.getObject().getCurrencies(
                GetCurrenciesRequest.newBuilder()
                        .setFilter(CurrenciesFilter.newBuilder()
                                .addAllCurrencyIds(ids)
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(0)
                                .setCount(ids.size())
                                .build())
                        .build()
        );

        log.info("Loaded currencies by IDs: {}", ids);

        return currencyMapper.toDtoList(currencies.getDataList());
    }
}
