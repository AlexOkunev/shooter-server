package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceEquipmentCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;

public interface ReferenceEquipmentService {
    ReferenceEquipment save(@Valid @NotNull SaveReferenceEquipmentCommand command);
}
