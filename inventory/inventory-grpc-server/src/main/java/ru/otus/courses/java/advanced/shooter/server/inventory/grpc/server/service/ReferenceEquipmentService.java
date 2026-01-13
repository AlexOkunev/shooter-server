package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.SaveReferenceEquipmentCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipment;

public interface ReferenceEquipmentService {
    ReferenceEquipment save(@Valid @NotNull SaveReferenceEquipmentCommand command);
}
