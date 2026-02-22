package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.EquipmentType;

import java.time.ZonedDateTime;

@Schema(
        name = "ProductTradeInfo",
        description = "Product trade information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ProductTradeDto {

    @Schema(description = "Player UUID", example = "2b4b2d5e-0a39-4a6e-9aa6-4cbe4c1c2b51")
    private String playerUuid;

    @Schema(description = "Trade UUID", example = "9d9c9e9a-8f0e-4e1b-b85e-90c4bf2e0d5d")
    private String uuid;

    @Schema(description = "Product id", example = "101")
    private Integer productId;

    @Schema(description = "Product equipment type")
    private EquipmentType equipmentType;

    @Schema(description = "Purchased equipment amount", example = "1")
    private Integer equipmentAmount;

    @Schema(description = "Purchased equipment")
    private ProductEquipmentDto equipment;

    @Schema(description = "Price value (amount in currency units)", example = "500")
    private Integer priceValue;

    @Schema(description = "Price currency")
    private CurrencyDto priceCurrency;

    @Schema(description = "Created timestamp", example = "2025-01-16T09:10:00+01:00")
    private ZonedDateTime createdTimestamp;

    @Schema(description = "Updated timestamp", example = "2025-01-16T09:10:00+01:00")
    private ZonedDateTime updatedTimestamp;

    @Schema(description = "Trade status", example = "ISSUE_EQUIPMENT_PENDING")
    private ProductTradeStatus status;
}
