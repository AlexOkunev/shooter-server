package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;

import java.time.ZonedDateTime;

@Schema(
        name = "PlayerAccountLogEntry",
        description = "Player account operation log entry"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAccountLogEntryDto {

    @Schema(description = "Player UUID", example = "2b4b2d5e-0a39-4a6e-9aa6-4cbe4c1c2b51")
    private String playerUuid;

    @Schema(description = "Currency")
    private CurrencyDto currency;

    @Schema(description = "Operation type", example = "BUY")
    private OperationType operationType;

    @Schema(description = "Amount before operation", example = "1000")
    private Integer amountBefore;

    @Schema(description = "Amount after operation", example = "850")
    private Integer amountAfter;

    @Schema(description = "Log entry UUID", example = "9d9c9e9a-8f0e-4e1b-b85e-90c4bf2e0d5d")
    private String uuid;

    @Schema(description = "Operation UUID", example = "a8d4a1a4-0a0f-4c72-aab3-2bd3b7f2a111", nullable = true)
    private String operationUuid;

    @Schema(
            description = "Date and time",
            example = "2025-01-15T11:20:30+01:00",
            format = "date-time"
    )
    private ZonedDateTime timestamp;
}