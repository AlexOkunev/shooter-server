package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceEquipmentCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.ReferenceEquipmentMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ReferenceEquipmentRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ReferenceEquipmentService;

@Validated
@Service
@RequiredArgsConstructor
public class ReferenceEquipmentServiceImpl implements ReferenceEquipmentService {

    private final ReferenceEquipmentRepository referenceEquipmentRepository;
    private final ReferenceEquipmentMapper referenceEquipmentMapper;

    @Override
    @Transactional
    public ReferenceEquipment save(@Valid @NotNull SaveReferenceEquipmentCommand command) {
        ReferenceEquipment referenceEquipment = referenceEquipmentMapper.toEntity(command);
        return referenceEquipmentRepository.save(referenceEquipment);
    }
}
