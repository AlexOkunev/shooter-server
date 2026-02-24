package ru.otus.courses.java.advanced.shooter.server.gateway.player.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundleDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.MoneyBundlePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.MoneyBundleWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.CurrencyDtoCachingMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.MoneyBundleService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfoListPage;

@Slf4j
@Tag(name = "Market / Money bundles API")
@RestController
@RequestMapping("/market/money-bundles")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class MoneyBundleCachingController {

    private final MoneyBundleService moneyBundleService;
    private final MoneyBundleWithDtoContextMapper moneyBundleMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyDtoCachingMappingContextFactory currencyMappingContextFactory;

    @GetMapping("/{id}")
    @Operation(summary = "Get money bundle by ID")
    public Mono<MoneyBundleDto> fetchOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
        Mono<MoneyBundleInfo> moneyBundleInfoMono = moneyBundleService.fetchOne(id)
                .cache();

        Mono<CurrencyDtoMappingContext> contextMono = currencyMappingContextFactory.createForMoneyBundle(moneyBundleInfoMono);

        return Mono.zip(moneyBundleInfoMono, contextMono)
                .map(tuple -> moneyBundleMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping
    @Operation(summary = "Get money bundles list page")
    public Mono<MoneyBundlePageResponseDto> fetchPage(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest,
            @RequestParam(required = false) Integer currencyId
    ) {
        PaginationRequest paginationRequestProto = paginationRequestMapper.toProto(paginationRequest);

        Mono<MoneyBundleInfoListPage> moneyBundlesMono = currencyId != null
                ? moneyBundleService.fetchMoneyBundlesPageByCurrencyId(currencyId, paginationRequestProto).cache()
                : moneyBundleService.fetchMoneyBundlesPage(paginationRequestProto).cache();

        Mono<CurrencyDtoMappingContext> contextMono = currencyMappingContextFactory.createForMoneyBundles(moneyBundlesMono);

        return Mono.zip(moneyBundlesMono, contextMono)
                .map(tuple -> moneyBundleMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }
}
