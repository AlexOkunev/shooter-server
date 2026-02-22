package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.ProductTradeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.ProductTradePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.ProductTradeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.ProductTradeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.CurrencyMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.EquipmentMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.ProductTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

@Slf4j
@Tag(name = "Player / Market / Product trades API")
@RestController
@RequestMapping("/player/{playerUuid}/market/product-trades")
@RequiredArgsConstructor
public class ProductTradeController {

    private final ProductTradeService productTradeService;
    private final ProductTradeMapper productTradeMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyMappingContextFactory currencyMappingContextFactory;
    private final EquipmentMappingContextFactory equipmentMappingContextFactory;

    @GetMapping("/{tradeUuid}")
    @Operation(summary = "Get product trade by UUID")
    public Mono<ProductTradeDto> fetchOne(@PathVariable String playerUuid, @PathVariable String tradeUuid) {
        Mono<ProductTradeInfo> productTradeInfoMono = productTradeService.fetchOne(playerUuid, tradeUuid)
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
            @PathVariable String playerUuid,
            @RequestBody @NotNull @Valid ProductTradeSearchRequestDto requestDto
    ) {
        Mono<ProductTradeInfoListPage> productTradeInfoListPageMono = productTradeService.fetchPage(
                        playerUuid,
                        productTradeMapper.toProto(requestDto),
                        paginationRequestMapper.toProto(requestDto.getPaginationRequest())
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
