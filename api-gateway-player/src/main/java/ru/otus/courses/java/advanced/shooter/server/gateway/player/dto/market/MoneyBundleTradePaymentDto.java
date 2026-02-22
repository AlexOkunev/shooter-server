package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Schema(
        name = "MoneyBundleTradePayment",
        description = "Payment info for money bundle trade"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBundleTradePaymentDto {

    @Schema(description = "Payment session ID", example = "sess_123")
    private String paymentSession;

    @Schema(description = "Public token for client-side payment", example = "pub_token_abc")
    private String publicToken;

    @Schema(description = "Payment UUID", example = "a8d4a1a4-0a0f-4c72-aab3-2bd3b7f2a111")
    private String uuid;

    @Schema(
            description = "Payment start timestamp",
            example = "2025-01-16T09:10:00+01:00",
            format = "date-time"
    )
    private ZonedDateTime startTimestamp;

    @Schema(
            description = "Payment finish timestamp (optional)",
            nullable = true,
            example = "2025-01-16T09:10:00+01:00",
            format = "date-time"
    )
    private ZonedDateTime finishTimestamp;
}
