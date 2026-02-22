package ru.otus.courses.java.advanced.shooter.server.gateway.player.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.ProductTradeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.ProductTradePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.ProductTradeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.ProductTradeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.CurrencyMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.EquipmentMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.ProductTradeService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.player.PlayerService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

@Slf4j
@Tag(name = "Player / Market / Product trades API")
@RestController
@RequestMapping("/player/current/market/product-trades")
@RequiredArgsConstructor
public class ProductTradeController {

    private final PlayerService playerService;
    private final ProductTradeService productTradeService;
    private final ProductTradeMapper productTradeMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyMappingContextFactory currencyMappingContextFactory;
    private final EquipmentMappingContextFactory equipmentMappingContextFactory;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create product trade")
    public Mono<ProductTradeDto> create(@AuthenticationPrincipal Jwt jwt, @RequestParam Integer productId) {
        String keycloakId = jwt.getSubject();

        Mono<ProductTradeInfo> productTradeInfoMono = playerService.getPlayerInfo(keycloakId)
                .flatMap(playerInfo -> productTradeService.createTrade(playerInfo.getPlayerUuid(), productId))
                .cache();

        return Mono.zip(
                        productTradeInfoMono,
                        currencyMappingContextFactory.createForProductTrade(productTradeInfoMono),
                        equipmentMappingContextFactory.createForProductTrade(productTradeInfoMono)
                )
                .map(tuple ->
                        productTradeMapper.toDto(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @GetMapping("/{tradeUuid}")
    @Operation(summary = "Get product trade by UUID")
    public Mono<ProductTradeDto> fetchOne(@AuthenticationPrincipal Jwt jwt, @PathVariable String tradeUuid) {
        String keycloakId = jwt.getSubject();

        Mono<ProductTradeInfo> productTradeInfoMono = playerService.getPlayerInfo(keycloakId)
                .flatMap(playerInfo -> productTradeService.fetchOne(playerInfo.getPlayerUuid(), tradeUuid))
                .cache();

        return Mono.zip(
                        productTradeInfoMono,
                        currencyMappingContextFactory.createForProductTrade(productTradeInfoMono),
                        equipmentMappingContextFactory.createForProductTrade(productTradeInfoMono)
                )
                .map(tuple ->
                        productTradeMapper.toDto(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @PostMapping("/search")
    @Operation(summary = "Search product trades")
    public Mono<ProductTradePageResponseDto> fetchPage(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @NotNull @Valid ProductTradeSearchRequestDto requestDto
    ) {
        String keycloakId = jwt.getSubject();

        Mono<ProductTradeInfoListPage> productTradeInfoListPageMono = playerService.getPlayerInfo(keycloakId)
                .flatMap(playerInfo -> productTradeService.fetchPage(
                                playerInfo.getPlayerUuid(),
                                productTradeMapper.toProto(requestDto),
                                paginationRequestMapper.toProto(requestDto.getPaginationRequest())
                        )
                )
                .cache();

        return Mono.zip(
                        productTradeInfoListPageMono,
                        currencyMappingContextFactory.createForProductTrades(productTradeInfoListPageMono),
                        equipmentMappingContextFactory.createForProductTrades(productTradeInfoListPageMono)
                )
                .map(tuple ->
                        productTradeMapper.toPageDto(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }
}
