package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradeStatus;

import java.time.ZonedDateTime;

@Schema(
        name = "MoneyBundleTrade",
        description = "Money bundle trade information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBundleTradeDto {

    @Schema(description = "Player UUID", example = "2b4b2d5e-0a39-4a6e-9aa6-4cbe4c1c2b51")
    private String playerUuid;

    @Schema(description = "Trade UUID", example = "9d9c9e9a-8f0e-4e1b-b85e-90c4bf2e0d5d")
    private String uuid;

    @Schema(description = "Money bundle id", example = "10")
    private Integer moneyBundleId;

    @Schema(description = "Currency")
    private CurrencyDto currency;

    @Schema(description = "Currency amount", example = "1000")
    private Integer currencyAmount;

    @Schema(description = "Price in rubles", example = "199")
    private Integer rublesPrice;

    @Schema(description = "Created timestamp", example = "2025-01-16T09:10:00+01:00")
    private ZonedDateTime createdTimestamp;

    @Schema(description = "Updated timestamp", example = "2025-01-16T09:10:00+01:00")
    private ZonedDateTime updatedTimestamp;

    @Schema(description = "Trade status", example = "PAYMENT_PENDING")
    private MoneyBundleTradeStatus status;

    @Schema(description = "Payment info", nullable = true)
    private MoneyBundleTradePaymentDto paymentInfo;
}
