package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.AmmunitionMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AmmunitionRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GunRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AmmunitionService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.AmmunitionSpecifications;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.PaginationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AmmunitionServiceImpl implements AmmunitionService {
    private final AmmunitionRepository ammunitionRepository;

    private final AmmunitionMapper ammunitionMapper;

    private final GunRepository gunRepository;

    @Override
    @Transactional(readOnly = true)
    public AmmunitionInfo getAmmunitionInfo(int ammunitionId) {
        return ammunitionRepository.findById(ammunitionId)
                .map(ammunitionMapper::toResponseWithGuns)
                .orElseThrow(() -> new ObjectNotFoundException("Ammunition with id '%d' not found".formatted(ammunitionId)));
    }

    @Override
    @Transactional(readOnly = true)
    public AmmunitionInfo getEnabledAmmunitionInfo(int ammunitionId) {
        return ammunitionRepository.findByIdAndEnabledIsTrue(ammunitionId)
                .map(ammunitionMapper::toResponseWithEnabledGuns)
                .orElseThrow(() -> new ObjectNotFoundException("Ammunition with id '%d' not found".formatted(ammunitionId)));
    }

    @Override
    @Transactional
    public AmmunitionInfo createAmmunition(CreateAmmunitionRequest request) {
        if (!request.hasData()) {
            throw new InvalidRequestException("Data must not be empty");
        }

        AmmunitionWritableData data = request.getData();
        validateAmmunitionWritableData(data);

        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIdsList());
        validateCompatibleGunsList(compatibleGuns, data);

        Ammunition ammunition = ammunitionMapper.toEntity(data, compatibleGuns);
        ammunition = ammunitionRepository.save(ammunition);

        return ammunitionMapper.toResponseWithGuns(ammunition);
    }

    @Override
    @Transactional
    public AmmunitionInfo updateAmmunition(UpdateAmmunitionRequest request) {
        Ammunition ammunition = ammunitionRepository.findById(request.getAmmunitionId())
                .orElseThrow(() -> new ObjectNotFoundException("Ammunition with id '%d' not found".formatted(request.getAmmunitionId())));

        if (!request.hasData()) {
            return ammunitionMapper.toResponseWithGuns(ammunition);
        }

        AmmunitionWritableData data = request.getData();
        validateAmmunitionWritableData(data);

        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIdsList());
        validateCompatibleGunsList(compatibleGuns, data);

        ammunitionMapper.updateAmmunition(ammunition, data, compatibleGuns);
        ammunition = ammunitionRepository.save(ammunition);

        return ammunitionMapper.toResponseWithGuns(ammunition);
    }

    private void validateAmmunitionWritableData(AmmunitionWritableData data) {
        if (StringUtils.isBlank(data.getName())) {
            throw new InvalidRequestException("Name cannot be blank or null");
        }

        if (data.getSpeed() <= 0) {
            throw new InvalidRequestException("Speed must be greater than 0");
        }

        if (data.getDamageMeanValue() <= 0) {
            throw new InvalidRequestException("Damage mean value must be greater than 0");
        }

        if (data.getDamageVariance() <= 0) {
            throw new InvalidRequestException("Damage variance must be greater than 0");
        }
    }

    private static void validateCompatibleGunsList(List<Gun> compatibleGuns, AmmunitionWritableData data) {
        List<Integer> compatibleGunIds = compatibleGuns.stream().map(Gun::getId).toList();
        List<Integer> notFoundGunIds = ListUtils.subtract(data.getCompatibleGunIdsList(), compatibleGunIds);

        if (!notFoundGunIds.isEmpty()) {
            throw new InvalidRequestException("Compatible guns with ids %s not found".formatted(StringUtils.join(notFoundGunIds)));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AmmunitionInfoListPage getAmmunitionList(GetAmmunitionListRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<Ammunition> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PaginationUtils.getPageable(request.getPaginationRequest(), Sort.by(Sort.Direction.ASC, Ammunition.Fields.id)) :
                PaginationUtils.getPageable(0, 10, Sort.by(Sort.Direction.ASC, Ammunition.Fields.id));

        Page<Ammunition> data = ammunitionRepository.findAll(specification, pageable);

        Page<AmmunitionInfo> mappedData = switch (request.getCompatibleGunsInclusionMode()) {
            case DONT_INCLUDE, UNRECOGNIZED -> data.map(ammunitionMapper::toResponse);
            case INCLUDE_ONLY_ENABLED -> data.map(ammunitionMapper::toResponseWithEnabledGuns);
            case INCLUDE_ALL -> data.map(ammunitionMapper::toResponseWithGuns);
        };

        return AmmunitionInfoListPage.newBuilder()
                .addAllData(mappedData)
                .setTotalCount(data.getTotalElements())
                .build();
    }

    private static Specification<Ammunition> getSpecification(AmmunitionFilter filter) {
        List<Specification<Ammunition>> specifications = new ArrayList<>();

        if (filter.hasEnabled()) {
            specifications.add(AmmunitionSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.hasName()) {
            specifications.add(AmmunitionSpecifications.byNameStartsWith(filter.getName()));
        }

        if (filter.getCompatibleGunIdCount() > 0) {
            specifications.add(
                    filter.getOnlyEnabledCompatibleGuns()
                            ? AmmunitionSpecifications.byEnabledCompatibleGunIds(filter.getCompatibleGunIdList())
                            : AmmunitionSpecifications.byCompatibleGunIds(filter.getCompatibleGunIdList())
            );
        }

        if (filter.getAmmunitionIdCount() > 0) {
            specifications.add(AmmunitionSpecifications.byAmmunitionIds(filter.getAmmunitionIdList()));
        }

        return Specification.allOf(specifications);
    }
}
