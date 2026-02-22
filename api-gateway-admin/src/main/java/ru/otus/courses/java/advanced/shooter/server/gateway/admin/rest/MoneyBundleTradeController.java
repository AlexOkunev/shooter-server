package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.MoneyBundleTradeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.CurrencyMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.MoneyBundleTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfoListPage;

@Slf4j
@Tag(name = "Player / Market / Money bundle trades API")
@RestController
@RequestMapping("/player/{playerUuid}/market/money-bundle-trades")
@RequiredArgsConstructor
public class MoneyBundleTradeController {

    private final MoneyBundleTradeService moneyBundleTradeService;
    private final MoneyBundleTradeMapper moneyBundleTradeMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyMappingContextFactory currencyMappingContextFactory;

    @GetMapping("/{tradeUuid}")
    @Operation(summary = "Get money bundle trade by UUID")
    public Mono<MoneyBundleTradeDto> fetchOne(@PathVariable String playerUuid, @PathVariable String tradeUuid) {
        Mono<MoneyBundleTradeInfo> moneyBundleTradeInfoMono = moneyBundleTradeService.fetchOne(playerUuid, tradeUuid)
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
            @PathVariable String playerUuid,
            @RequestBody @NotNull @Valid MoneyBundleTradeSearchRequestDto requestDto
    ) {
        Mono<MoneyBundleTradeInfoListPage> moneyBundleTradeInfoListPageMono = moneyBundleTradeService.fetchPage(
                        playerUuid,
                        moneyBundleTradeMapper.toProto(requestDto),
                        paginationRequestMapper.toProto(requestDto.getPaginationRequest())
                )
                .cache();

        return Mono.zip(
                        moneyBundleTradeInfoListPageMono,
                        currencyMappingContextFactory.createForMoneyBundleTrades(moneyBundleTradeInfoListPageMono)
                )
                .map(tuple -> moneyBundleTradeMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }
}
