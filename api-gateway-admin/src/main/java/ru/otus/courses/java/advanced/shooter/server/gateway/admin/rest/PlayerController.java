package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.player.PlayerDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.player.PlayersSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.player.PlayerMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.player.PlayerService;

@Slf4j
@Tag(name = "Players API")
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @GetMapping("/{playerUuid}")
    @Operation(summary = "Get player information")
    public Mono<PlayerDto> getPlayerInfo(@PathVariable String playerUuid) {
        return playerService.getPlayerInfo(playerUuid)
                .map(playerMapper::toDto);
    }

    @PostMapping
    @Operation(summary = "Search players by filter")
    public Mono<PageResponseDto<PlayerDto>> searchPlayers(@RequestBody @NotNull @Valid PlayersSearchRequestDto request) {
        return playerService.searchPlayers(
                        playerMapper.toProto(request),
                        paginationRequestMapper.toProto(request.getPaginationRequest())
                )
                .map(playerMapper::toPageDto);
    }
}
