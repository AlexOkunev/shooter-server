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
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.AttachmentMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.AttachmentRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GunRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.AttachmentService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.AttachmentSpecifications;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.PaginationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;

    private final GunRepository gunRepository;

    @Override
    @Transactional(readOnly = true)
    public AttachmentInfo getAttachmentInfo(int attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .map(attachmentMapper::toResponseWithGuns)
                .orElseThrow(() -> new ObjectNotFoundException("Attachment with id '%d' not found".formatted(attachmentId)));
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentInfo getEnabledAttachmentInfo(int attachmentId) {
        return attachmentRepository.findByIdAndEnabledIsTrue(attachmentId)
                .map(attachmentMapper::toResponseWithEnabledGuns)
                .orElseThrow(() -> new ObjectNotFoundException("Attachment with id '%d' not found".formatted(attachmentId)));
    }

    @Override
    @Transactional
    public AttachmentInfo createAttachment(CreateAttachmentRequest request) {
        if (!request.hasData()) {
            throw new InvalidRequestException("Data must not be empty");
        }

        AttachmentWritableData data = request.getData();
        validateAttachmentWritableData(data);

        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIdsList());
        validateCompatibleGunsList(compatibleGuns, data);

        Attachment attachment = attachmentMapper.toEntity(data, compatibleGuns);
        attachment = attachmentRepository.save(attachment);

        return attachmentMapper.toResponseWithGuns(attachment);
    }

    @Override
    @Transactional
    public AttachmentInfo updateAttachment(UpdateAttachmentRequest request) {
        Attachment attachment = attachmentRepository.findById(request.getAttachmentId())
                .orElseThrow(() -> new ObjectNotFoundException("Attachment with id '%d' not found".formatted(request.getAttachmentId())));

        if (!request.hasData()) {
            return attachmentMapper.toResponseWithGuns(attachment);
        }

        AttachmentWritableData data = request.getData();
        validateAttachmentWritableData(data);

        List<Gun> compatibleGuns = gunRepository.findAllById(data.getCompatibleGunIdsList());
        validateCompatibleGunsList(compatibleGuns, data);

        attachmentMapper.updateAttachment(attachment, data, compatibleGuns);
        attachment = attachmentRepository.save(attachment);

        return attachmentMapper.toResponseWithGuns(attachment);
    }

    private void validateAttachmentWritableData(AttachmentWritableData data) {
        if (StringUtils.isBlank(data.getName())) {
            throw new InvalidRequestException("Name cannot be blank or null");
        }

        if (data.getType() == AttachmentType.UNRECOGNIZED) {
            throw new InvalidRequestException("Type can not be unrecognized");
        }

        if (!data.hasEffect()) {
            throw new InvalidRequestException("Effect can not be empty");
        }

        if (data.getEffect().getLaserMaxDistanceMeters() < 0) {
            throw new InvalidRequestException("Laser max distance cannot be negative");
        }
    }

    private static void validateCompatibleGunsList(List<Gun> compatibleGuns, AttachmentWritableData data) {
        List<Integer> compatibleGunIds = compatibleGuns.stream().map(Gun::getId).toList();
        List<Integer> notFoundGunIds = ListUtils.subtract(data.getCompatibleGunIdsList(), compatibleGunIds);

        if (!notFoundGunIds.isEmpty()) {
            throw new InvalidRequestException("Compatible guns with ids %s not found".formatted(StringUtils.join(notFoundGunIds)));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentInfoListPage getAttachments(GetAttachmentsRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<Attachment> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PaginationUtils.getPageable(request.getPaginationRequest(), Sort.by(Sort.Direction.ASC, Attachment.Fields.id)) :
                PaginationUtils.getPageable(0, 10, Sort.by(Sort.Direction.ASC, Attachment.Fields.id));

        Page<Attachment> data = attachmentRepository.findAll(specification, pageable);

        Page<AttachmentInfo> mappedData = switch (request.getCompatibleGunsInclusionMode()) {
            case DONT_INCLUDE, UNRECOGNIZED -> data.map(attachmentMapper::toResponse);
            case INCLUDE_ONLY_ENABLED -> data.map(attachmentMapper::toResponseWithEnabledGuns);
            case INCLUDE_ALL -> data.map(attachmentMapper::toResponseWithGuns);
        };

        return AttachmentInfoListPage.newBuilder()
                .addAllData(mappedData)
                .setTotalCount(data.getTotalElements())
                .build();
    }

    private static Specification<Attachment> getSpecification(AttachmentsFilter filter) {
        List<Specification<Attachment>> specifications = new ArrayList<>();

        if (filter.hasEnabled()) {
            specifications.add(AttachmentSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.hasName()) {
            specifications.add(AttachmentSpecifications.byNameStartsWith(filter.getName()));
        }

        if (filter.hasType()) {
            specifications.add(AttachmentSpecifications.byType(filter.getType()));
        }

        if (filter.getCompatibleGunIdCount() > 0) {
            specifications.add(
                    filter.getOnlyEnabledCompatibleGuns()
                            ? AttachmentSpecifications.byEnabledCompatibleGunIds(filter.getCompatibleGunIdList())
                            : AttachmentSpecifications.byCompatibleGunIds(filter.getCompatibleGunIdList())
            );
        }

        if (filter.getAttachmentIdCount() > 0) {
            specifications.add(AttachmentSpecifications.byAttachmentIds(filter.getAttachmentIdList()));
        }

        return Specification.allOf(specifications);
    }
}
