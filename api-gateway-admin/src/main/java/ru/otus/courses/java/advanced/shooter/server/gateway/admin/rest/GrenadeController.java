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
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadePageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadeSaveRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.GrenadeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.GrenadeService;

@Slf4j
@Tag(name = "Equipment / Grenades API")
@RestController
@RequestMapping("/equipment/grenades")
@RequiredArgsConstructor
public class GrenadeController {

    private final GrenadeService grenadeService;
    private final GrenadeMapper grenadeMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @PostMapping
    @Operation(summary = "Create grenade")
    public Mono<GrenadeDto> create(@RequestBody @Valid @NotNull GrenadeSaveRequestDto dto) {
        return grenadeService.create(grenadeMapper.toProto(dto))
                .map(grenadeMapper::toDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get grenade")
    public Mono<GrenadeDto> getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
        return grenadeService.getOne(id)
                .map(grenadeMapper::toDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update grenade")
    public Mono<GrenadeDto> update(@PathVariable int id, @RequestBody @NotNull @Valid GrenadeSaveRequestDto dto) {
        return grenadeService.update(id, grenadeMapper.toProto(dto))
                .map(grenadeMapper::toDto);
    }

    @PostMapping("/search")
    @Operation(summary = "Search grenades by filter")
    public Mono<GrenadePageResponseDto> search(@RequestBody @NotNull @Valid GrenadeSearchRequestDto request) {
        return grenadeService.search(
                        grenadeMapper.toProto(request),
                        paginationRequestMapper.toProto(request.getPaginationRequest())
                )
                .map(grenadeMapper::toPageDto);
    }
}
