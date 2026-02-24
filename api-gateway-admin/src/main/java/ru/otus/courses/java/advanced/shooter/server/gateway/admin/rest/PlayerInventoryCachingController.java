package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.PlayerInventoryItemsPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.PlayerInventoryLogPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.EquipmentTypeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.PlayerInventoryItemWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.PlayerInventoryLogWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.PlayerInventoryDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.factory.PlayerInventoryDtoCachingMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory.PlayerInventoryLogService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

@Slf4j
@Tag(name = "Player inventory API")
@RestController
@RequestMapping("/players/{playerUuid}/inventory")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class PlayerInventoryCachingController {

    private final PlayerInventoryService playerInventoryService;
    private final PlayerInventoryLogService playerInventoryLogService;
    private final PlayerInventoryItemWithDtoContextMapper playerInventoryItemMapper;
    private final PlayerInventoryLogWithDtoContextMapper playerInventoryLogMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final EquipmentTypeMapper equipmentTypeMapper;
    private final PlayerInventoryDtoCachingMappingContextFactory playerInventoryMappingContextFactory;

    @GetMapping
    @Operation(summary = "Get player inventory")
    public Mono<PlayerInventoryItemsPageResponseDto> getInventory(
            @PathVariable String playerUuid,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        Mono<PlayerInventoryItemsPage> itemsPageMono = playerInventoryService.getPlayerInventoryItemsPage(
                        playerUuid,
                        paginationRequestMapper.toProto(paginationRequest)
                )
                .cache();

        Mono<PlayerInventoryDtoMappingContext> contextMono = playerInventoryMappingContextFactory.createForItems(itemsPageMono);

        return Mono.zip(itemsPageMono, contextMono)
                .map(tuple -> playerInventoryItemMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @PostMapping
    @Operation(summary = "Initialize player inventory")
    public Mono<Void> initializeInventory(@PathVariable String playerUuid) {
        return playerInventoryService.initializeInventory(playerUuid)
                .then();
    }

    @GetMapping("/log")
    @Operation(summary = "Get player inventory log")
    public Mono<PlayerInventoryLogPageResponseDto> getInventoryLog(
            @PathVariable String playerUuid,
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        Mono<PlayerInventoryLogPage> logPageMono = playerInventoryLogService.getPlayerInventoryLogPage(
                        playerUuid,
                        paginationRequestMapper.toProto(paginationRequest)
                )
                .cache();

        Mono<PlayerInventoryDtoMappingContext> contextMono = playerInventoryMappingContextFactory.createForLog(logPageMono);

        return Mono.zip(logPageMono, contextMono)
                .map(tuple -> playerInventoryLogMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @PostMapping("/{equipmentType}/{equipmentId}")
    @Operation(summary = "Give equipment to player")
    public Mono<Void> giveEquipment(
            @Parameter(description = "Player UUID") @PathVariable String playerUuid,
            @Parameter(description = "Equipment type. Value from enum") @PathVariable EquipmentType equipmentType,
            @Parameter(description = "Equipment ID") @PathVariable int equipmentId,
            @Parameter(description = "Equipment amount. Must be positive") @Positive @RequestParam int amount
    ) {
        return playerInventoryService.giveEquipment(
                        playerUuid,
                        equipmentTypeMapper.toProto(equipmentType),
                        equipmentId,
                        amount
                )
                .then();
    }

    @DeleteMapping("/{equipmentType}/{equipmentId}")
    @Operation(summary = "Give equipment to player")
    public Mono<Void> takeAwayEquipment(
            @Parameter(description = "Player UUID") @PathVariable String playerUuid,
            @Parameter(description = "Equipment type. Value from enum") @PathVariable EquipmentType equipmentType,
            @Parameter(description = "Equipment ID") @PathVariable int equipmentId,
            @Parameter(description = "Equipment amount. Must be positive") @Positive @RequestParam int amount
    ) {
        return playerInventoryService.takeAwayEquipment(
                        playerUuid,
                        equipmentTypeMapper.toProto(equipmentType),
                        equipmentId,
                        amount
                )
                .then();
    }
}
