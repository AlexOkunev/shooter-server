package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.player;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Schema(
        name = "PlayerDto",
        description = "Player account information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {

    @Schema(
            description = "Unique player identifier",
            example = "550e8400-e29b-41d4-a716-446655440000",
            format = "uuid"
    )
    private String playerUuid;

    @Schema(
            description = "Player email address",
            example = "player@example.com"
    )
    private String email;

    @Schema(
            description = "Player first name",
            example = "John"
    )
    private String firstName;

    @Schema(
            description = "Player last name",
            example = "Doe"
    )
    private String lastName;

    @Schema(
            description = "Player login",
            example = "john.doe"
    )
    private String login;

    @Schema(
            description = "Indicates if the player account is enabled",
            example = "true"
    )
    private Boolean enabled;

    @Schema(
            description = "Account creation time with time zone.",
            example = "2025-01-15T11:20:30+01:00",
            format = "date-time"
    )
    private ZonedDateTime createdAt;
}