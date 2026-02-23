package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.player;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;

import java.util.List;

@Schema(
        name = "PlayersFilterDto",
        description = "Filter for players"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayersSearchRequestDto {

    @Schema(
            description = "List of player UUIDs",
            example = "[\"550e8400-e29b-41d4-a716-446655440000\"]"
    )
    private List<String> playerUuids;

    @Schema(
            description = "Player login prefix",
            example = "player.login"
    )
    private String login;

    @Schema(
            description = "Player email prefix",
            example = "aa_mail@mail.ru"
    )
    private String email;

    @Schema(description = "Player is enabled", example = "true")
    private Boolean enabled;

    @Schema(description = "Pagination request")
    @NotNull
    @Valid
    private PaginationRequestDto paginationRequest;
}
