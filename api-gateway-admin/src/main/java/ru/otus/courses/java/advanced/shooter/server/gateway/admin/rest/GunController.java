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
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GunDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GunPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GunSaveRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GunSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.GunMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.GunService;

@Slf4j
@Tag(name = "Equipment / Gun API")
@RestController
@RequestMapping("/equipment/guns")
@RequiredArgsConstructor
public class GunController {

    private final GunService gunService;
    private final GunMapper gunMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @PostMapping
    @Operation(summary = "Create gun")
    public Mono<GunDto> create(@RequestBody @Valid @NotNull GunSaveRequestDto dto) {
        return gunService.create(gunMapper.toProto(dto))
                .map(gunMapper::toDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gun")
    public Mono<GunDto> getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
        return gunService.getOne(id)
                .map(gunMapper::toDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update gun")
    public Mono<GunDto> update(@PathVariable int id, @RequestBody @NotNull @Valid GunSaveRequestDto dto) {
        return gunService.update(id, gunMapper.toProto(dto))
                .map(gunMapper::toDto);
    }

    @PostMapping("/search")
    @Operation(summary = "Search gun by filter")
    public Mono<GunPageResponseDto> search(@RequestBody @NotNull @Valid GunSearchRequestDto request) {
        return gunService.search(
                        gunMapper.toProto(request),
                        paginationRequestMapper.toProto(request.getPaginationRequest())
                )
                .map(gunMapper::toPageDto);
    }
}
