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
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionSaveRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.AmmunitionMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.AmmunitionService;

@Slf4j
@Tag(name = "Equipment / Ammunition API")
@RestController
@RequestMapping("/equipment/ammunition")
@RequiredArgsConstructor
public class AmmunitionController {

    private final AmmunitionService ammunitionService;
    private final AmmunitionMapper ammunitionMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @PostMapping
    @Operation(summary = "Create ammunition")
    public Mono<AmmunitionDto> create(@RequestBody @Valid @NotNull AmmunitionSaveRequestDto dto) {
        return ammunitionService.create(ammunitionMapper.toProto(dto))
                .map(ammunitionMapper::toDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ammunition")
    public Mono<AmmunitionDto> getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
        return ammunitionService.getOne(id)
                .map(ammunitionMapper::toDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ammunition")
    public Mono<AmmunitionDto> update(@PathVariable int id, @RequestBody @NotNull @Valid AmmunitionSaveRequestDto dto) {
        return ammunitionService.update(id, ammunitionMapper.toProto(dto))
                .map(ammunitionMapper::toDto);
    }

    @PostMapping("/search")
    @Operation(summary = "Search ammunition by filter")
    public Mono<AmmunitionPageResponseDto> search(@RequestBody @NotNull @Valid AmmunitionSearchRequestDto request) {
        return ammunitionService.search(
                        ammunitionMapper.toProto(request),
                        paginationRequestMapper.toProto(request.getPaginationRequest())
                )
                .map(ammunitionMapper::toPageDto);
    }
}
