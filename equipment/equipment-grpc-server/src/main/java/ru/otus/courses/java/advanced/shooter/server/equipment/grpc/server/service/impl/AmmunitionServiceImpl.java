package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain.AmmunitionMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AmmunitionRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GunRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AmmunitionService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.AmmunitionSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class AmmunitionServiceImpl implements AmmunitionService {
    private final AmmunitionRepository ammunitionRepository;
    private final AmmunitionMapper ammunitionMapper;
    private final GunRepository gunRepository;

    @Override
    @Transactional(readOnly = true)
    public Ammunition getAmmunition(int ammunitionId) {
        return ammunitionRepository.findWithGunsById(ammunitionId)
                .orElseThrow(() -> new ObjectNotFoundException("Ammunition with id '%d' not found".formatted(ammunitionId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Ammunition getEnabledAmmunition(int ammunitionId) {
        return ammunitionRepository.findWithEnabledBunsByIdAndEnabledIsTrue(ammunitionId)
                .orElseThrow(() -> new ObjectNotFoundException("Ammunition with id '%d' not found".formatted(ammunitionId)));
    }

    @Override
    @Transactional
    public Ammunition createAmmunition(@Valid @NotNull AmmunitionSavedData data) {
        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIds());
        validateCompatibleGunsList(compatibleGuns, data);

        Ammunition ammunition = ammunitionMapper.toEntity(data, compatibleGuns);

        return ammunitionRepository.save(ammunition);
    }

    @Override
    @Transactional
    public Ammunition updateAmmunition(int ammunitionId, @Valid @NotNull AmmunitionSavedData data) {
        Ammunition ammunition = ammunitionRepository.findById(ammunitionId)
                .orElseThrow(() -> new ObjectNotFoundException("Ammunition with id '%d' not found".formatted(ammunitionId)));

        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIds());
        validateCompatibleGunsList(compatibleGuns, data);

        ammunitionMapper.updateEntity(ammunition, data, compatibleGuns);

        return ammunitionRepository.save(ammunition);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ammunition> getAmmunitionList(
            @NotNull AmmunitionFilterParams filterParams,
            @NotNull Pageable pageable,
            @NotNull RelatedEntitiesInclusionMode relatedEntitiesInclusionMode
    ) {
        Specification<Ammunition> specification = getSpecification(filterParams);

        Page<Ammunition> data = ammunitionRepository.findAll(specification, pageable);

        List<Integer> ammunitionIds = data.stream()
                .map(Ammunition::getId)
                .toList();

        //Related entities fetch based on the idea from https://thorben-janssen.com/hibernate-warning-firstresult-maxresults
        //Variable is not used. Only JPA side effect is used
        List<Ammunition> dataWithRelated =
                switch (relatedEntitiesInclusionMode) {
                    case INCLUDE_ONLY_ENABLED -> ammunitionRepository.findWithEnabledGunsAllByIdIn(ammunitionIds);
                    case INCLUDE_ALL -> ammunitionRepository.findWithGunsAllByIdIn(ammunitionIds);
                    case DONT_INCLUDE -> data.getContent();
                };

        return data;
    }

    private static Specification<Ammunition> getSpecification(@NotNull AmmunitionFilterParams filter) {
        List<Specification<Ammunition>> specifications = new ArrayList<>();

        if (filter.getEnabled() != null) {
            specifications.add(AmmunitionSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.getName() != null) {
            specifications.add(AmmunitionSpecifications.byNameStartsWith(filter.getName()));
        }

        if (!filter.getCompatibleGunIds().isEmpty()) {
            specifications.add(
                    filter.isOnlyEnabledCompatibleGuns()
                            ? AmmunitionSpecifications.byEnabledCompatibleGunIds(filter.getCompatibleGunIds())
                            : AmmunitionSpecifications.byCompatibleGunIds(filter.getCompatibleGunIds())
            );
        }

        if (!filter.getAmmunitionIds().isEmpty()) {
            specifications.add(AmmunitionSpecifications.byAmmunitionIds(filter.getAmmunitionIds()));
        }

        if (filter.getUpdatedAfter() != null) {
            specifications.add(AmmunitionSpecifications.byUpdatedAfter(filter.getUpdatedAfter()));
        }

        return Specification.allOf(specifications);
    }

    private static void validateCompatibleGunsList(List<Gun> compatibleGuns, AmmunitionSavedData data) {
        Set<Integer> compatibleGunIds = compatibleGuns.stream()
                .map(Gun::getId)
                .collect(Collectors.toSet());

        Set<Integer> notFoundGunIds = SetUtils.difference(data.getCompatibleGunIds(), compatibleGunIds);

        if (!notFoundGunIds.isEmpty()) {
            throw new InvalidRequestException(
                    "Compatible guns with ids %s not found".formatted(StringUtils.join(notFoundGunIds))
            );
        }
    }
}
