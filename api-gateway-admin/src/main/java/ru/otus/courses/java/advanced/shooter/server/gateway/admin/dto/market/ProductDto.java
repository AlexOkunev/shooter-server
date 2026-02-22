package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.CurrencyDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.ProductEquipmentDto;

import java.time.ZonedDateTime;

@Schema(
        name = "Product",
        description = "Market product information"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ProductDto {

    @Schema(description = "Product id", example = "10")
    private int id;

    @Schema(description = "Product equipment type")
    private EquipmentType equipmentType;

    @Schema(description = "Product equipment")
    private ProductEquipmentDto equipment;

    @Schema(description = "Product equipment amount")
    private int equipmentAmount;

    @Schema(description = "Price value (amount in currency units)", example = "500")
    private int priceValue;

    @Schema(description = "Price currency")
    private CurrencyDto priceCurrency;

    @Schema(description = "Enabled flag", example = "true")
    private boolean enabled;

    @Schema(description = "Created timestamp", example = "2025-01-15T11:20:30+01:00")
    private ZonedDateTime createdTimestamp;

    @Schema(description = "Updated timestamp", example = "2025-01-15T11:20:30+01:00")
    private ZonedDateTime updatedTimestamp;

    @Schema(description = "Version", example = "2")
    private int version;
}
