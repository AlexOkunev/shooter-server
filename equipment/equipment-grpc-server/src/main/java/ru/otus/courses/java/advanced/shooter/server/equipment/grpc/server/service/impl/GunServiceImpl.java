package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain.GunMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AmmunitionRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AttachmentRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GunRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GunService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.GunSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GunServiceImpl implements GunService {
    private final GunRepository gunRepository;
    private final GunMapper gunMapper;
    private final AmmunitionRepository ammunitionRepository;
    private final AttachmentRepository attachmentRepository;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, Gun.Fields.id);

    @Override
    @Transactional(readOnly = true)
    public Gun getGun(int gunId) {
        return gunRepository.findWithRelatedEntitiesById(gunId)
                .orElseThrow(() -> new ObjectNotFoundException("Gun with id '%d' not found".formatted(gunId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Gun getEnabledGun(int gunId) {
        return gunRepository.findWithEnabledRelatedEntitiesByIdAndEnabled(gunId, true)
                .orElseThrow(() -> new ObjectNotFoundException("Gun with id '%d' not found".formatted(gunId)));
    }

    @Override
    @Transactional
    public Gun createGun(@Valid @NotNull GunSavedData data) {
        List<Ammunition> ammunitionList = ammunitionRepository.findAllById(data.getCompatibleAmmunitionIds());
        validateCompatibleAmmunitionList(ammunitionList, data);

        List<Attachment> attachmentList = attachmentRepository.findAllById(data.getCompatibleAttachmentIds());
        validateCompatibleAttachmentList(attachmentList, data);

        Gun gun = gunMapper.toEntity(data, ammunitionList, attachmentList);

        return gunRepository.save(gun);
    }

    @Override
    @Transactional
    public Gun updateGun(int gunId, @Valid @NotNull GunSavedData data) {
        Gun gun = gunRepository.findById(gunId)
                .orElseThrow(() -> new ObjectNotFoundException("Gun with id '%d' not found".formatted(gunId)));

        List<Ammunition> ammunitionList = ammunitionRepository.findAllById(data.getCompatibleAmmunitionIds());
        validateCompatibleAmmunitionList(ammunitionList, data);

        List<Attachment> attachmentList = attachmentRepository.findAllById(data.getCompatibleAttachmentIds());
        validateCompatibleAttachmentList(attachmentList, data);

        gunMapper.updateGun(gun, data, ammunitionList, attachmentList);

        return gunRepository.save(gun);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Gun> getGuns(
            @NotNull GunFilterParams filterParams,
            @NotNull Pageable pageable,
            @NotNull RelatedEntitiesInclusionMode relatedEntitiesInclusionMode
    ) {
        Specification<Gun> specification = getSpecification(filterParams);

        Page<Gun> data = gunRepository.findAll(specification, pageable);

        List<Integer> gunIds = data.stream()
                .map(Gun::getId)
                .toList();

        //Related entities fetch based on the idea from https://thorben-janssen.com/hibernate-warning-firstresult-maxresults
        //and https://thorben-janssen.com/fix-multiplebagfetchexception-hibernate
        //Variables are not used. Only JPA side effect is used

        List<Gun> gunsWithAmmunition = switch (relatedEntitiesInclusionMode) {
            case INCLUDE_ONLY_ENABLED -> gunRepository.findWithEnabledAmmunitionAllByIdIn(gunIds);
            case INCLUDE_ALL -> gunRepository.findWithAmmunitionAllByIdIn(gunIds);
            case DONT_INCLUDE -> List.of();
        };

        List<Gun> gunsWithAttachments = switch (relatedEntitiesInclusionMode) {
            case INCLUDE_ONLY_ENABLED -> gunRepository.findWithEnabledAttachmentsAllByIdIn(gunIds);
            case INCLUDE_ALL -> gunRepository.findWithAttachmentsAllByIdIn(gunIds);
            case DONT_INCLUDE -> List.of();
        };

        return data;
    }

    private static Specification<Gun> getSpecification(GunFilterParams filter) {
        List<Specification<Gun>> specifications = new ArrayList<>();

        if (filter.getEnabled() != null) {
            specifications.add(GunSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.getName() != null) {
            specifications.add(GunSpecifications.byNameStartsWith(filter.getName()));
        }

        if (filter.getType() != null) {
            specifications.add(GunSpecifications.byType(filter.getType()));
        }

        if (!filter.getGunIds().isEmpty()) {
            specifications.add(GunSpecifications.byGunIds(filter.getGunIds()));
        }

        if (filter.getUpdatedAfter() != null) {
            specifications.add(GunSpecifications.byUpdatedAfter(filter.getUpdatedAfter()));
        }

        return Specification.allOf(specifications);
    }

    private static void validateCompatibleAmmunitionList(List<Ammunition> compatibleAmmunitionSet, GunSavedData data) {
        Set<Integer> compatibleAmmunitionIds = compatibleAmmunitionSet.stream()
                .map(Ammunition::getId)
                .collect(Collectors.toSet());

        Set<Integer> notFoundAmmunitionIds = SetUtils.difference(data.getCompatibleAmmunitionIds(), compatibleAmmunitionIds);

        if (!notFoundAmmunitionIds.isEmpty()) {
            throw new InvalidRequestException("Compatible ammunition with ids %s not found".formatted(StringUtils.join(notFoundAmmunitionIds)));
        }
    }

    private static void validateCompatibleAttachmentList(List<Attachment> compatibleAttachmentList, GunSavedData data) {
        Set<Integer> compatibleAttachmentIds = compatibleAttachmentList.stream()
                .map(Attachment::getId)
                .collect(Collectors.toSet());

        Set<Integer> notFoundAttachmentIds = SetUtils.difference(data.getCompatibleAttachmentIds(), compatibleAttachmentIds);

        if (!notFoundAttachmentIds.isEmpty()) {
            throw new InvalidRequestException("Compatible attachments with ids %s not found".formatted(StringUtils.join(notFoundAttachmentIds)));
        }
    }
}
