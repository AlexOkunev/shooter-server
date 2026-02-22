package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.PlayerAccountItemDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.PlayerAccountItemPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.PlayerAccountLogPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.PlayerAccountItemMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.PlayerAccountLogMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.CurrencyMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.PlayerAccountLogService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;

@Slf4j
@Tag(name = "Player account API")
@RestController
@RequestMapping("/players/{playerUuid}/account")
@RequiredArgsConstructor
public class PlayerAccountController {

    private final PlayerAccountService playerAccountService;
    private final PlayerAccountItemMapper playerAccountItemMapper;
    private final PlayerAccountLogService playerAccountLogService;
    private final PlayerAccountLogMapper playerAccountLogMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyMappingContextFactory currencyMappingContextFactory;

    @GetMapping
    @Operation(summary = "Get player account")
    public Mono<PlayerAccountItemPageResponseDto> getAccount(
            @PathVariable String playerUuid,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        Mono<PlayerAccountItemsPage> itemsPageMono = playerAccountService.getPlayerAccountItemsPage(
                        playerUuid,
                        paginationRequestMapper.toProto(paginationRequest)
                )
                .cache();

        Mono<CurrencyMappingContext> contextMono = currencyMappingContextFactory.createForItems(itemsPageMono);

        return Mono.zip(itemsPageMono, contextMono)
                .map(tuple -> playerAccountItemMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @PostMapping
    @Operation(summary = "Initialize player account")
    public Mono<Void> initializeAccount(@PathVariable String playerUuid) {
        return playerAccountService.initializeAccount(playerUuid)
                .then();
    }

    @PostMapping("/currency/{currencyId}")
    @Operation(summary = "Give currency to player")
    public Mono<PlayerAccountItemDto> giveEquipment(
            @Parameter(description = "Player UUID") @PathVariable String playerUuid,
            @Parameter(description = "Currency ID") @PathVariable int currencyId,
            @Parameter(description = "Currency amount. Must be positive") @Positive @RequestParam int amount
    ) {
        Mono<PlayerAccountItemInfo> itemMono = playerAccountService.giveCurrency(playerUuid, currencyId, amount)
                .cache();

        Mono<CurrencyMappingContext> contextMono = currencyMappingContextFactory.createForItem(itemMono);

        return Mono.zip(itemMono, contextMono)
                .map(tuple -> playerAccountItemMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @DeleteMapping("/currency/{currencyId}")
    @Operation(summary = "Give currency to player")
    public Mono<PlayerAccountItemDto> takeAwayEquipment(
            @Parameter(description = "Player UUID") @PathVariable String playerUuid,
            @Parameter(description = "Currency ID") @PathVariable int currencyId,
            @Parameter(description = "Currency amount. Must be positive") @Positive @RequestParam int amount
    ) {
        Mono<PlayerAccountItemInfo> itemMono = playerAccountService.takeAwayCurrency(playerUuid, currencyId, amount)
                .cache();

        Mono<CurrencyMappingContext> contextMono = currencyMappingContextFactory.createForItem(itemMono);

        return Mono.zip(itemMono, contextMono)
                .map(tuple -> playerAccountItemMapper.toDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping("/log")
    @Operation(summary = "Get player account log")
    public Mono<PlayerAccountLogPageResponseDto> getAccountLog(
            @PathVariable String playerUuid,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        Mono<PlayerAccountLogPage> logPageMono = playerAccountLogService.getPlayerAccountLogPage(
                        playerUuid,
                        paginationRequestMapper.toProto(paginationRequest)
                )
                .cache();

        Mono<CurrencyMappingContext> contextMono = currencyMappingContextFactory.createForLog(logPageMono);

        return Mono.zip(logPageMono, contextMono)
                .map(tuple -> playerAccountLogMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }
}
