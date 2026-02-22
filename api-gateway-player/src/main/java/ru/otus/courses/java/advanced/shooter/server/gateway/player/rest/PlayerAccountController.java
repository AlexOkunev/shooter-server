package ru.otus.courses.java.advanced.shooter.server.gateway.player.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountItemPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountLogPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.PlayerAccountItemMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.PlayerAccountLogMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.CurrencyMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.PlayerAccountLogService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.player.PlayerService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;

@Slf4j
@Tag(name = "Player account API")
@RestController
@RequestMapping("/players/current/account")
@RequiredArgsConstructor
public class PlayerAccountController {

    private final PlayerService playerService;
    private final PlayerAccountService playerAccountService;
    private final PlayerAccountItemMapper playerAccountItemMapper;
    private final PlayerAccountLogService playerAccountLogService;
    private final PlayerAccountLogMapper playerAccountLogMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyMappingContextFactory currencyMappingContextFactory;

    @GetMapping
    @Operation(summary = "Get current player account")
    public Mono<PlayerAccountItemPageResponseDto> getAccount(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        String keycloakId = jwt.getSubject();

        Mono<PlayerAccountItemsPage> itemsPageMono = playerService.getPlayerInfo(keycloakId)
                .flatMap(playerInfo -> playerAccountService.getPlayerAccountItemsPage(
                        playerInfo.getPlayerUuid(),
                        paginationRequestMapper.toProto(paginationRequest)
                ))
                .cache();

        Mono<CurrencyMappingContext> contextMono = currencyMappingContextFactory.createForItems(itemsPageMono);

        return Mono.zip(itemsPageMono, contextMono)
                .map(tuple -> playerAccountItemMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping("/log")
    @Operation(summary = "Get current player account log")
    public Mono<PlayerAccountLogPageResponseDto> getAccountLog(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        String keycloakId = jwt.getSubject();

        Mono<PlayerAccountLogPage> logPageMono = playerService.getPlayerInfo(keycloakId)
                .flatMap(playerInfo -> playerAccountLogService.getPlayerAccountLogPage(
                        playerInfo.getPlayerUuid(),
                        paginationRequestMapper.toProto(paginationRequest)
                ))
                .cache();

        Mono<CurrencyMappingContext> contextMono = currencyMappingContextFactory.createForLog(logPageMono);

        return Mono.zip(logPageMono, contextMono)
                .map(tuple -> playerAccountLogMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }
}
