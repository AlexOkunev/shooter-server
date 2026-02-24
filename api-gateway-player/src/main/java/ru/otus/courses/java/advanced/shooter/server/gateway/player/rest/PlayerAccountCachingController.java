
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountItemPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.PlayerAccountLogPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.PlayerAccountItemWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.PlayerAccountLogWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.CurrencyDtoCachingMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.PlayerCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.PlayerAccountLogService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;

@Slf4j
@Tag(name = "Player account API")
@RestController
@RequestMapping("/players/current/account")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class PlayerAccountCachingController {

    private final PlayerCacheService playerCacheService;
    private final PlayerAccountService playerAccountService;
    private final PlayerAccountItemWithDtoContextMapper playerAccountItemMapper;
    private final PlayerAccountLogService playerAccountLogService;
    private final PlayerAccountLogWithDtoContextMapper playerAccountLogMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyDtoCachingMappingContextFactory currencyMappingContextFactory;

    @GetMapping
    @Operation(summary = "Get current player account")
    public Mono<PlayerAccountItemPageResponseDto> getAccount(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        String keycloakId = jwt.getSubject();

        Mono<PlayerAccountItemsPage> itemsPageMono =
                Mono.fromCallable(() -> playerCacheService.getByKeycloakId(keycloakId))
                        .flatMap(playerInfo -> playerAccountService.getPlayerAccountItemsPage(
                                playerInfo.getPlayerUuid(),
                                paginationRequestMapper.toProto(paginationRequest)
                        ))
                        .cache();

        return Mono.zip(
                        itemsPageMono,
                        currencyMappingContextFactory.createForItems(itemsPageMono)
                )
                .map(tuple -> playerAccountItemMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping("/log")
    @Operation(summary = "Get current player account log")
    public Mono<PlayerAccountLogPageResponseDto> getAccountLog(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        String keycloakId = jwt.getSubject();

        Mono<PlayerAccountLogPage> logPageMono =
                Mono.fromCallable(() -> playerCacheService.getByKeycloakId(keycloakId))
                        .flatMap(playerInfo -> playerAccountLogService.getPlayerAccountLogPage(
                                playerInfo.getPlayerUuid(),
                                paginationRequestMapper.toProto(paginationRequest)
                        ))
                        .cache();

        return Mono.zip(
                        logPageMono,
                        currencyMappingContextFactory.createForLog(logPageMono)
                )
                .map(tuple -> playerAccountLogMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }
}
