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
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory.PlayerInventoryItemsPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory.PlayerInventoryLogPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.PlayerInventoryItemMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.PlayerInventoryLogMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.PlayerInventoryMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.factory.PlayerInventoryMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.inventory.PlayerInventoryLogService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.inventory.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.player.PlayerService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

@Slf4j
@Tag(name = "Player inventory API")
@RestController
@RequestMapping("/players/current/inventory")
@RequiredArgsConstructor
public class PlayerInventoryController {

    private final PlayerService playerService;
    private final PlayerInventoryService playerInventoryService;
    private final PlayerInventoryLogService playerInventoryLogService;
    private final PlayerInventoryItemMapper playerInventoryItemMapper;
    private final PlayerInventoryLogMapper playerInventoryLogMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final PlayerInventoryMappingContextFactory playerInventoryMappingContextFactory;

    @GetMapping
    @Operation(summary = "Get current player inventory")
    public Mono<PlayerInventoryItemsPageResponseDto> getInventory(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        String keycloakId = jwt.getSubject();

        Mono<PlayerInventoryItemsPage> itemsPageMono = playerService.getPlayerInfo(keycloakId)
                .flatMap(playerInfo -> playerInventoryService.getPlayerInventoryItemsPage(
                        playerInfo.getPlayerUuid(),
                        paginationRequestMapper.toProto(paginationRequest)
                ))
                .cache();

        Mono<PlayerInventoryMappingContext> contextMono = playerInventoryMappingContextFactory.createForItems(itemsPageMono);

        return Mono.zip(itemsPageMono, contextMono)
                .map(tuple -> playerInventoryItemMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @GetMapping("/log")
    @Operation(summary = "Get current player inventory log")
    public Mono<PlayerInventoryLogPageResponseDto> getInventoryLog(
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        String keycloakId = jwt.getSubject();

        Mono<PlayerInventoryLogPage> logPageMono = playerService.getPlayerInfo(keycloakId)
                .flatMap(playerInfo -> playerInventoryLogService.getPlayerInventoryLogPage(
                        playerInfo.getPlayerUuid(),
                        paginationRequestMapper.toProto(paginationRequest)
                ))
                .cache();

        Mono<PlayerInventoryMappingContext> contextMono = playerInventoryMappingContextFactory.createForLog(logPageMono);

        return Mono.zip(logPageMono, contextMono)
                .map(tuple -> playerInventoryLogMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }
}
