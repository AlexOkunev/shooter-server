package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;

public interface GrenadeService {

    Grenade getGrenade(int grenadeId);

    Grenade getEnabledGrenade(int grenadeId);

    Grenade createGrenade(@Valid @NotNull GrenadeSavedData data);

    Grenade updateGrenade(int grenadeId, @Valid @NotNull GrenadeSavedData data);

    Page<Grenade> getGrenades(@NotNull GrenadeFilterParams filterParams, @NotNull Pageable pageable);
}
