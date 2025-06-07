package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceEquipmentId {
    private Integer equipmentId;

    private ProductEquipmentType equipmentType;
}
