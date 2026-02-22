package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.SearchInitialPlayerInventoryRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial.InitialPlayerInventoryItemsPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial.UpdateInitialPlayerInventoryRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.InitialInventoryMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.PlayerInventoryMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.factory.PlayerInventoryMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory.InitialInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

@Slf4j
@Tag(name = "Initial player inventory API")
@RestController
@RequestMapping("/initial-inventory")
@RequiredArgsConstructor
public class InitialInventoryController {

    private final InitialInventoryService initialInventoryService;
    private final InitialInventoryMapper initialInventoryMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final PlayerInventoryMappingContextFactory playerInventoryMappingContextFactory;

    @PostMapping("/search")
    @Operation(summary = "Get initial player inventory")
    public Mono<InitialPlayerInventoryItemsPageResponseDto> getInventory(
            @RequestBody @NotNull @Valid SearchInitialPlayerInventoryRequestDto requestDto
    ) {
        Mono<InitialPlayerInventoryItemsPage> itemsPageMono = initialInventoryService.getInitialInventoryItemsPage(
                        initialInventoryMapper.toProto(requestDto),
                        paginationRequestMapper.toProto(requestDto.getPaginationRequest())
                )
                .cache();

        Mono<PlayerInventoryMappingContext> contextMono = playerInventoryMappingContextFactory.createForInitialItems(itemsPageMono);

        return Mono.zip(itemsPageMono, contextMono)
                .map(tuple -> initialInventoryMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @PatchMapping
    @Operation(summary = "Modify initial player inventory")
    public Mono<Void> getInventory(@RequestBody @Valid @NotNull UpdateInitialPlayerInventoryRequestDto dto) {
        UpdateInitialPlayerInventoryRequest request = initialInventoryMapper.toProto(dto);
        return initialInventoryService.updateInitialInventory(request)
                .then();
    }
}
