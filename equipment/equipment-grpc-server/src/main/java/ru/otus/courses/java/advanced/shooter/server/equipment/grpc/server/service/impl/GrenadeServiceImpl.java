package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain.GrenadeMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GrenadeRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GrenadeService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.GrenadeSpecifications;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class GrenadeServiceImpl implements GrenadeService {
    private final GrenadeRepository grenadeRepository;
    private final GrenadeMapper grenadeMapper;

    @Override
    public Grenade getGrenade(int grenadeId) {
        return grenadeRepository.findById(grenadeId)
                .orElseThrow(() -> new ObjectNotFoundException("Grenade with id '%d' not found".formatted(grenadeId)));
    }

    @Override
    public Grenade getEnabledGrenade(int grenadeId) {
        return grenadeRepository.findByIdAndEnabled(grenadeId, true)
                .orElseThrow(() -> new ObjectNotFoundException("Grenade with id '%d' not found".formatted(grenadeId)));
    }

    @Override
    public Grenade createGrenade(@Valid @NotNull GrenadeSavedData data) {
        Grenade grenade = grenadeMapper.toEntity(data);
        return grenadeRepository.save(grenade);
    }

    @Override
    public Grenade updateGrenade(int grenadeId, @Valid @NotNull GrenadeSavedData data) {
        Grenade grenade = grenadeRepository.findById(grenadeId)
                .orElseThrow(() -> new ObjectNotFoundException("Grenade with id '%d' not found".formatted(grenadeId)));

        grenadeMapper.updateGrenade(grenade, data);

        return grenadeRepository.save(grenade);
    }

    @Override
    public Page<Grenade> getGrenades(@NotNull GrenadeFilterParams filterParams, @NotNull Pageable pageable) {
        Specification<Grenade> specification = getSpecification(filterParams);
        return grenadeRepository.findAll(specification, pageable);
    }

    private static Specification<Grenade> getSpecification(GrenadeFilterParams filter) {
        List<Specification<Grenade>> specifications = new ArrayList<>();

        if (filter.getEnabled() != null) {
            specifications.add(GrenadeSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.getName() != null) {
            specifications.add(GrenadeSpecifications.byNameStartsWith(filter.getName()));
        }

        if (!filter.getGrenadeIds().isEmpty()) {
            specifications.add(GrenadeSpecifications.byGrenadeIds(filter.getGrenadeIds()));
        }

        if (filter.getUpdatedAfter() != null) {
            specifications.add(GrenadeSpecifications.byUpdatedAfter(filter.getUpdatedAfter()));
        }

        return Specification.allOf(specifications);
    }
}
