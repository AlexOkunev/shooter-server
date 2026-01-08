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
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain.AttachmentMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AttachmentRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GunRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AttachmentService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.AttachmentSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final GunRepository gunRepository;

    @Override
    @Transactional(readOnly = true)
    public Attachment getAttachment(int attachmentId) {
        return attachmentRepository.findWithGunsById(attachmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Attachment with id '%d' not found".formatted(attachmentId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Attachment getEnabledAttachment(int attachmentId) {
        return attachmentRepository.findWithEnabledGunsByIdAndEnabledIsTrue(attachmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Attachment with id '%d' not found".formatted(attachmentId)));
    }

    @Override
    @Transactional
    public Attachment createAttachment(@Valid @NotNull AttachmentSavedData data) {
        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIds());
        validateCompatibleGunsList(compatibleGuns, data);

        Attachment attachment = attachmentMapper.toEntity(data, compatibleGuns);

        return attachmentRepository.save(attachment);
    }

    @Override
    @Transactional
    public Attachment updateAttachment(int attachmentId, @Valid @NotNull AttachmentSavedData data) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Attachment with id '%d' not found".formatted(attachmentId)));

        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIds());
        validateCompatibleGunsList(compatibleGuns, data);

        attachmentMapper.updateEntity(attachment, data, compatibleGuns);

        return attachmentRepository.save(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attachment> getAttachments(@NotNull AttachmentFilterParams filterParams,
                                           @NotNull Pageable pageable,
                                           @NotNull RelatedEntitiesInclusionMode relatedEntitiesInclusionMode) {
        Specification<Attachment> specification = getSpecification(filterParams);

        Page<Attachment> data = attachmentRepository.findAll(specification, pageable);

        List<Integer> attachmentIds = data.stream()
                .map(Attachment::getId)
                .toList();

        //Related entities fetch based on the idea from https://thorben-janssen.com/hibernate-warning-firstresult-maxresults
        //Variable is not used. Only JPA side effect is used
        List<Attachment> dataWithRelated = switch (relatedEntitiesInclusionMode) {
            case INCLUDE_ONLY_ENABLED -> attachmentRepository.findWithEnabledGunsAllByIdIn(attachmentIds);
            case INCLUDE_ALL -> attachmentRepository.findWithGunsAllByIdIn(attachmentIds);
            case DONT_INCLUDE -> List.of();
        };

        return data;
    }

    private static Specification<Attachment> getSpecification(AttachmentFilterParams filter) {
        List<Specification<Attachment>> specifications = new ArrayList<>();

        if (filter.getEnabled() != null) {
            specifications.add(AttachmentSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.getName() != null) {
            specifications.add(AttachmentSpecifications.byNameStartsWith(filter.getName()));
        }

        if (filter.getType() != null) {
            specifications.add(AttachmentSpecifications.byType(filter.getType()));
        }

        if (!filter.getCompatibleGunIds().isEmpty()) {
            specifications.add(
                    filter.isOnlyEnabledCompatibleGuns()
                            ? AttachmentSpecifications.byEnabledCompatibleGunIds(filter.getCompatibleGunIds())
                            : AttachmentSpecifications.byCompatibleGunIds(filter.getCompatibleGunIds())
            );
        }

        if (!filter.getAttachmentIds().isEmpty()) {
            specifications.add(AttachmentSpecifications.byAttachmentIds(filter.getAttachmentIds()));
        }

        if (filter.getUpdatedAfter() != null) {
            specifications.add(AttachmentSpecifications.byUpdatedAfter(filter.getUpdatedAfter()));
        }

        return Specification.allOf(specifications);
    }

    private static void validateCompatibleGunsList(List<Gun> compatibleGuns, AttachmentSavedData data) {
        Set<Integer> compatibleGunIds = compatibleGuns.stream()
                .map(Gun::getId)
                .collect(Collectors.toSet());

        Set<Integer> notFoundGunIds = SetUtils.difference(data.getCompatibleGunIds(), compatibleGunIds);

        if (!notFoundGunIds.isEmpty()) {
            throw new InvalidRequestException("Compatible guns with ids %s not found".formatted(StringUtils.join(notFoundGunIds)));
        }
    }
}
