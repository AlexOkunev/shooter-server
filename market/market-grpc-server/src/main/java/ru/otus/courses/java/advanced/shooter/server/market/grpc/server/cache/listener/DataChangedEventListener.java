package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.MoneyBundleCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ProductCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.event.MoneyBundleChangedEvent;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.event.ProductChangedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataChangedEventListener {

    private final ProductCacheService productCacheService;

    private final MoneyBundleCacheService moneyBundleCacheService;

    @TransactionalEventListener(fallbackExecution = true)
    public void handleProductChangedEvent(ProductChangedEvent event) {
        log.info("Product changed event: {}", event);
        productCacheService.deleteById(event.productId());
    }

    @TransactionalEventListener(fallbackExecution = true)
    public void handleMoneyBundleChangedEvent(MoneyBundleChangedEvent event) {
        log.info("Money bundle changed event: {}", event);
        moneyBundleCacheService.deleteById(event.moneyBundleId());
    }
}