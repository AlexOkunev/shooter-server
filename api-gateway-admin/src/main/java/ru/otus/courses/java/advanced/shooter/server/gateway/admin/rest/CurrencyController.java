package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencySaveRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencySearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.CurrencyMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.CurrencyService;

@Slf4j
@Tag(name = "Equipment / Currencies API")
@RestController
@RequestMapping("/equipment/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @PostMapping
    @Operation(summary = "Create currency")
    public Mono<CurrencyDto> create(@RequestBody @Valid @NotNull CurrencySaveRequestDto dto) {
        return currencyService.create(currencyMapper.toProto(dto))
                .map(currencyMapper::toDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get currency")
    public Mono<CurrencyDto> getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
        return currencyService.getOne(id)
                .map(currencyMapper::toDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update currency")
    public Mono<CurrencyDto> update(@PathVariable int id, @RequestBody @NotNull @Valid CurrencySaveRequestDto dto) {
        return currencyService.update(id, currencyMapper.toProto(dto))
                .map(currencyMapper::toDto);
    }

    @PostMapping("/search")
    @Operation(summary = "Search currencies by filter")
    public Mono<PageResponseDto<CurrencyDto>> search(@RequestBody @NotNull @Valid CurrencySearchRequestDto request) {
        return currencyService.search(
                        currencyMapper.toProto(request),
                        paginationRequestMapper.toProto(request.getPaginationRequest())
                )
                .map(currencyMapper::toPageDto);
    }
}
