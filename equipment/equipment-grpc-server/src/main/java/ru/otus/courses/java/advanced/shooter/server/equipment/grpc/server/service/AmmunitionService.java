package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;

public interface AmmunitionService {

    Ammunition getAmmunition(int ammunitionId);

    Ammunition getEnabledAmmunition(int ammunitionId);

    Ammunition createAmmunition(@Valid @NotNull AmmunitionSavedData data);

    Ammunition updateAmmunition(int ammunitionId, @Valid @NotNull AmmunitionSavedData data);

    Page<Ammunition> getAmmunitionList(
            @NotNull AmmunitionFilterParams filterParams,
            @NotNull Pageable pageable,
            @NotNull RelatedEntitiesInclusionMode relatedEntitiesInclusionMode
    );
}