package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.CurrencyService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.market.PlayerAccountUtils;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrencyMappingContextFactory {

    private final CurrencyService currencyService;

    public Mono<CurrencyMappingContext> createForInitialItems(Mono<InitialPlayerAccountItemsPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage -> {
            Set<Integer> currencyIds = itemsPage.getDataList().stream()
                    .map(InitialPlayerAccountItemInfo::getCurrencyId)
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    public Mono<CurrencyMappingContext> createForItem(Mono<PlayerAccountItemInfo> itemsPageMono) {
        return itemsPageMono.flatMap(itemInfo ->
                create(Set.of(itemInfo.getCurrencyId())));
    }

    public Mono<CurrencyMappingContext> createForItems(Mono<PlayerAccountItemsPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage ->
                create(PlayerAccountUtils.getCurrencyIds(itemsPage)));
    }

    public Mono<CurrencyMappingContext> createForLog(Mono<PlayerAccountLogPage> logPageMono) {
        return logPageMono.flatMap(logPage ->
                create(PlayerAccountUtils.getCurrencyIds(logPage)));
    }

    public Mono<CurrencyMappingContext> createForMoneyBundle(Mono<MoneyBundleInfo> moneyBundleInfoMono) {
        return moneyBundleInfoMono.flatMap(moneyBundleInfo ->
                create(List.of(moneyBundleInfo.getCurrencyId())));
    }

    public Mono<CurrencyMappingContext> createForMoneyBundles(Mono<MoneyBundleInfoListPage> moneyBundleInfoListPageMono) {
        return moneyBundleInfoListPageMono.flatMap(moneyBundleInfoListPage -> {
            Set<Integer> currencyIds = moneyBundleInfoListPage.getDataList().stream()
                    .map(MoneyBundleInfo::getCurrencyId)
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    public Mono<CurrencyMappingContext> createForMoneyBundleTrade(Mono<MoneyBundleTradeInfo> moneyBundleTradeInfoMono) {
        return moneyBundleTradeInfoMono.flatMap(moneyBundleTradeInfo ->
                create(List.of(moneyBundleTradeInfo.getCurrencyId())));
    }

    public Mono<CurrencyMappingContext> createForMoneyBundleTrades(Mono<MoneyBundleTradeInfoListPage> moneyBundleTradeInfoListPageMono) {
        return moneyBundleTradeInfoListPageMono.flatMap(moneyBundleTradeInfoListPage -> {
            Set<Integer> currencyIds = moneyBundleTradeInfoListPage.getDataList().stream()
                    .map(MoneyBundleTradeInfo::getCurrencyId)
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    public Mono<CurrencyMappingContext> createForProduct(Mono<ProductInfo> productInfoMono) {
        return productInfoMono.flatMap(productInfo ->
                create(Set.of(productInfo.getPrice().getCurrencyId())));
    }

    public Mono<CurrencyMappingContext> createForProducts(Mono<ProductInfoListPage> productInfoListPageMono) {
        return productInfoListPageMono.flatMap(productInfoListPage -> {
            Set<Integer> currencyIds = productInfoListPage.getDataList().stream()
                    .map(productInfo -> productInfo.getPrice().getCurrencyId())
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    public Mono<CurrencyMappingContext> createForProductTrade(Mono<ProductTradeInfo> productTradeInfoMono) {
        return productTradeInfoMono.flatMap(productInfo ->
                create(Set.of(productInfo.getPriceCurrencyId())));
    }

    public Mono<CurrencyMappingContext> createForProductTrades(Mono<ProductTradeInfoListPage> productTradeInfoListPageMono) {
        return productTradeInfoListPageMono.flatMap(productInfoListPage -> {
            Set<Integer> currencyIds = productInfoListPage.getDataList().stream()
                    .map(ProductTradeInfo::getPriceCurrencyId)
                    .collect(Collectors.toSet());

            return create(currencyIds);
        });
    }

    private Mono<CurrencyMappingContext> create(Collection<Integer> currencyIds) {
        return currencyService.fetchByIds(currencyIds)
                .map(currencyInfoListPage -> currencyInfoListPage.getDataList()
                        .stream()
                        .collect(Collectors.toMap(CurrencyInfo::getId, Function.identity()))
                )
                .map(CurrencyMappingContext::new);
    }
}
