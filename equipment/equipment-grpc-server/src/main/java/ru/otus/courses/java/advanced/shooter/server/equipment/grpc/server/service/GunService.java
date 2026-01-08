package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

public interface GunService {

    Gun getGun(int gunId);

    Gun getEnabledGun(int gunId);

    Gun createGun(@Valid @NotNull GunSavedData data);

    Gun updateGun(int gunId, @Valid @NotNull GunSavedData data);

    Page<Gun> getGuns(
            @NotNull GunFilterParams filterParams,
            @NotNull Pageable pageable,
            @NotNull RelatedEntitiesInclusionMode relatedEntitiesInclusionMode
    );
}
