package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.GunMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AmmunitionRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AttachmentRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GunRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GunService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.GunSpecifications;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GunServiceImpl implements GunService {
    private final GunRepository gunRepository;

    private final GunMapper gunMapper;

    private final PaginationInfoMapper paginationInfoMapper;

    private final AmmunitionRepository ammunitionRepository;

    private final AttachmentRepository attachmentRepository;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, Gun.Fields.id);

    @Override
    @Transactional(readOnly = true)
    public GunInfo getGunInfo(int gunId) {
        return gunRepository.findById(gunId)
                .map(gunMapper::toResponseWithRelatedEntities)
                .orElseThrow(() -> new ObjectNotFoundException("Gun with id '%d' not found".formatted(gunId)));
    }

    @Override
    @Transactional(readOnly = true)
    public GunInfo getEnabledGunInfo(int gunId) {
        return gunRepository.findByIdAndEnabled(gunId, true)
                .map(gunMapper::toResponseWithEnabledRelatedEntities)
                .orElseThrow(() -> new ObjectNotFoundException("Gun with id '%d' not found".formatted(gunId)));
    }

    @Override
    @Transactional
    public GunInfo createGun(CreateGunRequest request) {
        if (!request.hasData()) {
            throw new InvalidRequestException("Data must not be empty");
        }

        validateGunWritableData(request.getData());

        List<Ammunition> ammunitionList = ammunitionRepository.findAllById(request.getData().getCompatibleAmmunitionIdsList());
        validateCompatibleAmmunitionList(ammunitionList, request.getData());

        List<Attachment> attachmentList = attachmentRepository.findAllById(request.getData().getCompatibleAttachmentIdsList());
        validateCompatibleAttachmentList(attachmentList, request.getData());

        Gun gun = gunMapper.toEntity(request.getData(), ammunitionList, attachmentList);
        gun = gunRepository.save(gun);

        return gunMapper.toResponseWithRelatedEntities(gun);
    }

    private void validateGunWritableData(GunWritableData data) {
        if (StringUtils.isBlank(data.getName())) {
            throw new InvalidRequestException("Name cannot be blank or null");
        }

        if (data.getWeightGrams() <= 0) {
            throw new InvalidRequestException("Weight must be greater than 0");
        }

        if (data.getRateOfFirePerMinute() <= 0) {
            throw new InvalidRequestException("Rate of fire must be greater than 0");
        }

        if (data.getType() == GunType.UNRECOGNIZED) {
            throw new InvalidRequestException("Unrecognized gun type");
        }
    }

    private static void validateCompatibleAmmunitionList(List<Ammunition> compatibleAmmunitionList, GunWritableData data) {
        List<Integer> compatibleAmmunitionIds = compatibleAmmunitionList.stream().map(Ammunition::getId).toList();
        List<Integer> notFoundAmmunitionIds = ListUtils.subtract(data.getCompatibleAmmunitionIdsList(), compatibleAmmunitionIds);

        if (!notFoundAmmunitionIds.isEmpty()) {
            throw new InvalidRequestException("Compatible ammunition with ids %s not found".formatted(StringUtils.join(notFoundAmmunitionIds)));
        }
    }

    private static void validateCompatibleAttachmentList(List<Attachment> compatibleAttachmentList, GunWritableData data) {
        List<Integer> compatibleAttachmentIds = compatibleAttachmentList.stream().map(Attachment::getId).toList();
        List<Integer> notFoundAttachmentIds = ListUtils.subtract(data.getCompatibleAttachmentIdsList(), compatibleAttachmentIds);

        if (!notFoundAttachmentIds.isEmpty()) {
            throw new InvalidRequestException("Compatible attachments with ids %s not found".formatted(StringUtils.join(notFoundAttachmentIds)));
        }
    }

    @Override
    @Transactional
    public GunInfo updateGun(UpdateGunRequest request) {
        Gun gun = gunRepository.findById(request.getGunId())
                .orElseThrow(() -> new ObjectNotFoundException("Gun with id '%d' not found".formatted(request.getGunId())));

        if (!request.hasData()) {
            return gunMapper.toResponse(gun);
        }

        validateGunWritableData(request.getData());

        List<Ammunition> ammunitionList = ammunitionRepository.findAllById(request.getData().getCompatibleAmmunitionIdsList());
        validateCompatibleAmmunitionList(ammunitionList, request.getData());

        List<Attachment> attachmentList = attachmentRepository.findAllById(request.getData().getCompatibleAttachmentIdsList());
        validateCompatibleAttachmentList(attachmentList, request.getData());

        gunMapper.updateGun(gun, request.getData(), ammunitionList, attachmentList);
        gun = gunRepository.save(gun);

        return gunMapper.toResponseWithRelatedEntities(gun);
    }

    @Override
    @Transactional
    public GunInfoListPage getGuns(GetGunsRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<Gun> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Page<Gun> data = gunRepository.findAll(specification, pageable);

        Page<GunInfo> mappedData = switch (request.getRelatedEntitiesInclusionMode()) {
            case DONT_INCLUDE, UNRECOGNIZED -> data.map(gunMapper::toResponse);
            case INCLUDE_ONLY_ENABLED -> data.map(gunMapper::toResponseWithEnabledRelatedEntities);
            case INCLUDE_ALL -> data.map(gunMapper::toResponseWithRelatedEntities);
        };

        return GunInfoListPage.newBuilder()
                .addAllData(mappedData)
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    private static Specification<Gun> getSpecification(GunsFilter filter) {
        List<Specification<Gun>> specifications = new ArrayList<>();

        if (filter.hasEnabled()) {
            specifications.add(GunSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.hasName()) {
            specifications.add(GunSpecifications.byNameStartsWith(filter.getName()));
        }

        if (filter.hasType()) {
            specifications.add(GunSpecifications.byType(filter.getType()));
        }

        if (filter.getGunIdCount() > 0) {
            specifications.add(GunSpecifications.byGunIds(filter.getGunIdList()));
        }

        return Specification.allOf(specifications);
    }
}
