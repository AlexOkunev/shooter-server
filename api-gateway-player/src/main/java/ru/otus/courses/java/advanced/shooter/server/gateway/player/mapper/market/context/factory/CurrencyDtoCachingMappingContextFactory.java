package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.CurrencyDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.market.PlayerAccountUtils;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class CurrencyDtoCachingMappingContextFactory {

    private final CurrencyDtoCacheService currencyDtoCacheService;

    public Mono<CurrencyDtoMappingContext> createForItem(Mono<PlayerAccountItemInfo> itemsPageMono) {
        return itemsPageMono.flatMap(itemInfo ->
                create(Set.of(itemInfo.getCurrencyId())));
    }

    public Mono<CurrencyDtoMappingContext> createForItems(Mono<PlayerAccountItemsPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage ->
                create(PlayerAccountUtils.getCurrencyIds(itemsPage)));
    }

    public Mono<CurrencyDtoMappingContext> createForLog(Mono<PlayerAccountLogPage> logPageMono) {
        return logPageMono.flatMap(logPage ->
                create(PlayerAccountUtils.getCurrencyIds(logPage)));
    }

    public Mono<CurrencyDtoMappingContext> createForMoneyBundle(Mono<MoneyBundleInfo> moneyBundleInfoMono) {
        return moneyBundleInfoMono.flatMap(moneyBundleInfo ->
                create(List.of(moneyBundleInfo.getCurrencyId())));
    }

    public Mono<CurrencyDtoMappingContext> createForMoneyBundles(Mono<MoneyBundleInfoListPage> moneyBundleInfoListPageMono) {
        return moneyBundleInfoListPageMono.flatMap(moneyBundleInfoListPage -> {
            Set<Integer> currencyIds = moneyBundleInfoListPage.getDataList().stream()
                    .map(MoneyBundleInfo::getCurrencyId)
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    public Mono<CurrencyDtoMappingContext> createForMoneyBundleTrade(Mono<MoneyBundleTradeInfo> moneyBundleTradeInfoMono) {
        return moneyBundleTradeInfoMono.flatMap(moneyBundleTradeInfo ->
                create(List.of(moneyBundleTradeInfo.getCurrencyId())));
    }

    public Mono<CurrencyDtoMappingContext> createForMoneyBundleTrades(Mono<MoneyBundleTradeInfoListPage> moneyBundleTradeInfoListPageMono) {
        return moneyBundleTradeInfoListPageMono.flatMap(moneyBundleTradeInfoListPage -> {
            Set<Integer> currencyIds = moneyBundleTradeInfoListPage.getDataList().stream()
                    .map(MoneyBundleTradeInfo::getCurrencyId)
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    public Mono<CurrencyDtoMappingContext> createForProduct(Mono<ProductInfo> productInfoMono) {
        return productInfoMono.flatMap(productInfo ->
                create(Set.of(productInfo.getPrice().getCurrencyId())));
    }

    public Mono<CurrencyDtoMappingContext> createForProducts(Mono<ProductInfoListPage> productInfoListPageMono) {
        return productInfoListPageMono.flatMap(productInfoListPage -> {
            Set<Integer> currencyIds = productInfoListPage.getDataList().stream()
                    .map(productInfo -> productInfo.getPrice().getCurrencyId())
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    public Mono<CurrencyDtoMappingContext> createForProductTrade(Mono<ProductTradeInfo> productTradeInfoMono) {
        return productTradeInfoMono.flatMap(productInfo ->
                create(Set.of(productInfo.getPriceCurrencyId())));
    }

    public Mono<CurrencyDtoMappingContext> createForProductTrades(Mono<ProductTradeInfoListPage> productTradeInfoListPageMono) {
        return productTradeInfoListPageMono.flatMap(productInfoListPage -> {
            Set<Integer> currencyIds = productInfoListPage.getDataList().stream()
                    .map(ProductTradeInfo::getPriceCurrencyId)
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    private Mono<CurrencyDtoMappingContext> create(Collection<Integer> currencyIds) {
        return Mono.fromCallable(() -> currencyDtoCacheService.getByIdsAsMap(currencyIds))
                .map(CurrencyDtoMappingContext::new);
    }
}
