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
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.player.PlayerDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.player.PlayersSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.player.PlayerMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.player.PlayerService;

@Slf4j
@Tag(name = "Players API")
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @GetMapping("/current")
    @Operation(summary = "Get current player information")
    public Mono<PlayerDto> getCurrentPlayer(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return playerService.getPlayerInfo(keycloakId)
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
