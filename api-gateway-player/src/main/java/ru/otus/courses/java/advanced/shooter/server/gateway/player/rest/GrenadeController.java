package ru.otus.courses.java.advanced.shooter.server.gateway.player.rest;

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
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GrenadeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.equipment.GrenadeService;

@Slf4j
@Tag(name = "Equipment / Grenades API")
@RestController
@RequestMapping("/equipment/grenades")
@RequiredArgsConstructor
public class GrenadeController {

    private final GrenadeService grenadeService;
    private final GrenadeMapper grenadeMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get grenade")
    public Mono<GrenadeDto> getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
        return grenadeService.getOne(id)
                .map(grenadeMapper::toDto);
    }

    @PostMapping("/search")
    @Operation(summary = "Search grenades by filter")
    public Mono<PageResponseDto<GrenadeDto>> search(@RequestBody @NotNull @Valid GrenadeSearchRequestDto request) {
        return grenadeService.search(
                        grenadeMapper.toProto(request),
                        paginationRequestMapper.toProto(request.getPaginationRequest())
                )
                .map(grenadeMapper::toPageDto);
    }
}
