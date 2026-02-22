package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial.InitialPlayerAccountItemsPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial.InitialPlayerAccountSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial.UpdateInitialPlayerAccountRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.InitialPlayerAccountMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.CurrencyMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.CurrencyMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.InitialPlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

@Slf4j
@Tag(name = "Initial player account API")
@RestController
@RequestMapping("/initial-account")
@RequiredArgsConstructor
public class InitialPlayerAccountController {

    private final InitialPlayerAccountService initialPlayerAccountService;
    private final InitialPlayerAccountMapper initialPlayerAccountMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyMappingContextFactory currencyMappingContextFactory;

    @PostMapping("/search")
    @Operation(summary = "Get initial player account")
    public Mono<InitialPlayerAccountItemsPageResponseDto> getInventory(
            @RequestBody @NotNull @Valid InitialPlayerAccountSearchRequestDto requestDto
    ) {
        Mono<InitialPlayerAccountItemsPage> itemsPageMono = initialPlayerAccountService.getInitialAccountItemsPage(
                        initialPlayerAccountMapper.toProto(requestDto),
                        paginationRequestMapper.toProto(requestDto.getPaginationRequest())
                )
                .cache();

        Mono<CurrencyMappingContext> contextMono = currencyMappingContextFactory.createForInitialItems(itemsPageMono);

        return Mono.zip(itemsPageMono, contextMono)
                .map(tuple -> initialPlayerAccountMapper.toPageDto(tuple.getT1(), tuple.getT2()));
    }

    @PatchMapping
    @Operation(summary = "Modify initial player account")
    public Mono<Void> getInventory(@RequestBody @Valid @NotNull UpdateInitialPlayerAccountRequestDto dto) {
        UpdateInitialPlayerAccountRequest request = initialPlayerAccountMapper.toProto(dto);
        return initialPlayerAccountService.updateInitialAccount(request)
                .then();
    }
}
