package ru.otus.courses.java.advanced.shooter.server.gateway.player.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleTradeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleTradePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleTradeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.MoneyBundleTradeWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.CurrencyDtoCachingMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.PlayerCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.MoneyBundleTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfoListPage;

@Slf4j
@Tag(name = "Player / Market / Money bundle trades API")
@RestController
@RequestMapping("/player/current/market/money-bundle-trades")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class MoneyBundleTradeCachingController {

    private final PlayerCacheService playerCacheService;
    private final MoneyBundleTradeService moneyBundleTradeService;
    private final MoneyBundleTradeWithDtoContextMapper moneyBundleTradeMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyDtoCachingMappingContextFactory currencyMappingContextFactory;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create money bundle trade")
    public Mono<MoneyBundleTradeDto> create(@AuthenticationPrincipal Jwt jwt, @RequestParam Integer moneyBundleId) {
        String keycloakId = jwt.getSubject();

        Mono<MoneyBundleTradeInfo> moneyBundleTradeInfoMono =
                Mono.fromCallable(() -> playerCacheService.getByKeycloakId(keycloakId))
                        .flatMap(playerInfo -> moneyBundleTradeService.createTrade(playerInfo.getPlayerUuid(), moneyBundleId))
                        .cache();

        return Mono.zip(
                        moneyBundleTradeInfoMono,
                        currencyMappingContextFactory.createForMoneyBundleTrade(moneyBundleTradeInfoMono)
                )
                .map(tuple -> moneyBundleTradeMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @PostMapping("/{tradeUuid}/payment")
    @Operation(summary = "Create money bundle trade")
    public Mono<MoneyBundleTradeDto> performPayment(@AuthenticationPrincipal Jwt jwt, @PathVariable String tradeUuid) {
        String keycloakId = jwt.getSubject();

        Mono<MoneyBundleTradeInfo> moneyBundleTradeInfoMono =
                Mono.fromCallable(() -> playerCacheService.getByKeycloakId(keycloakId))
                        .flatMap(playerInfo -> moneyBundleTradeService.performPayment(playerInfo.getPlayerUuid(), tradeUuid))
                        .cache();

        return Mono.zip(
                        moneyBundleTradeInfoMono,
                        currencyMappingContextFactory.createForMoneyBundleTrade(moneyBundleTradeInfoMono)
                )
                .map(tuple -> moneyBundleTradeMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping("/{tradeUuid}")
    @Operation(summary = "Get money bundle trade by UUID")
    public Mono<MoneyBundleTradeDto> fetchOne(@AuthenticationPrincipal Jwt jwt, @PathVariable String tradeUuid) {
        String keycloakId = jwt.getSubject();

        Mono<MoneyBundleTradeInfo> moneyBundleTradeInfoMono =
                Mono.fromCallable(() -> playerCacheService.getByKeycloakId(keycloakId))
                        .flatMap(playerInfo -> moneyBundleTradeService.fetchOne(playerInfo.getPlayerUuid(), tradeUuid))
                        .cache();

        return Mono.zip(
                        moneyBundleTradeInfoMono,
                        currencyMappingContextFactory.createForMoneyBundleTrade(moneyBundleTradeInfoMono)
                )
                .map(tuple -> moneyBundleTradeMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @PostMapping("/search")
    @Operation(summary = "Search money bundle trades")
    public Mono<MoneyBundleTradePageResponseDto> fetchPage(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @NotNull @Valid MoneyBundleTradeSearchRequestDto requestDto
    ) {
        String keycloakId = jwt.getSubject();

        Mono<MoneyBundleTradeInfoListPage> moneyBundleTradeInfoListPageMono =
                Mono.fromCallable(() -> playerCacheService.getByKeycloakId(keycloakId))
                        .flatMap(playerInfo -> moneyBundleTradeService.fetchPage(
                                        playerInfo.getPlayerUuid(),
                                        moneyBundleTradeMapper.toProto(requestDto),
                                        paginationRequestMapper.toProto(requestDto.getPaginationRequest())
                                )
                        )
                        .cache();

        return Mono.zip(
                        moneyBundleTradeInfoListPageMono,
                        currencyMappingContextFactory.createForMoneyBundleTrades(moneyBundleTradeInfoListPageMono)
                )
                .map(tuple -> moneyBundleTradeMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }
}
