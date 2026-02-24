package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundlePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.SaveMoneyBundleRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.MoneyBundleWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.CurrencyDtoCachingMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.MoneyBundleService;
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

    @PostMapping
    @Operation(summary = "Create money bundle")
    public Mono<MoneyBundleDto> create(@RequestBody @Valid @NotNull SaveMoneyBundleRequestDto requestDto) {
        Mono<MoneyBundleInfo> moneyBundleInfoMono = moneyBundleService.create(
                        moneyBundleMapper.toProto(requestDto)
                )
                .cache();

        Mono<CurrencyDtoMappingContext> contextMono = currencyMappingContextFactory.createForMoneyBundle(moneyBundleInfoMono);

        return Mono.zip(moneyBundleInfoMono, contextMono)
                .map(tuple -> moneyBundleMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get money bundle by ID")
    public Mono<MoneyBundleDto> fetchOne(@PathVariable int id) {
        Mono<MoneyBundleInfo> moneyBundleInfoMono = moneyBundleService.fetchOne(id)
                .cache();

        Mono<CurrencyDtoMappingContext> contextMono = currencyMappingContextFactory.createForMoneyBundle(moneyBundleInfoMono);

        return Mono.zip(moneyBundleInfoMono, contextMono)
                .map(tuple -> moneyBundleMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @PutMapping("/{moneyBundleId}")
    @Operation(summary = "Update money bundle")
    public Mono<MoneyBundleDto> update(
            @Parameter(description = "Money bundle ID") @PathVariable int moneyBundleId,
            @RequestBody @Valid @NotNull SaveMoneyBundleRequestDto requestDto,
            @Parameter(description = "Optimistic lock version") @RequestParam int version
    ) {
        Mono<MoneyBundleInfo> moneyBundleInfoMono = moneyBundleService.update(
                        moneyBundleId,
                        version,
                        moneyBundleMapper.toProto(requestDto)
                )
                .cache();

        Mono<CurrencyDtoMappingContext> contextMono = currencyMappingContextFactory.createForMoneyBundle(moneyBundleInfoMono);

        return Mono.zip(moneyBundleInfoMono, contextMono)
                .map(tuple -> moneyBundleMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping
    @Operation(summary = "Get money bundles list page")
    public Mono<MoneyBundlePageResponseDto> fetchPage(
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
