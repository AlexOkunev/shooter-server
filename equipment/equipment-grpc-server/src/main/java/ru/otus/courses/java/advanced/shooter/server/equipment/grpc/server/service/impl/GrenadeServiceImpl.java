package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.GrenadeMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.GrenadeRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.GrenadeService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.GrenadeSpecifications;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.PaginationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GrenadeServiceImpl implements GrenadeService {
    private final GrenadeRepository grenadeRepository;

    private final GrenadeMapper grenadeMapper;

    @Override
    public GrenadeInfo getGrenadeInfo(int grenadeId) {
        return grenadeRepository.findById(grenadeId)
                .map(grenadeMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Grenade with id '%d' not found".formatted(grenadeId)));
    }

    @Override
    public GrenadeInfo getEnabledGrenadeInfo(int grenadeId) {
        return grenadeRepository.findByIdAndEnabled(grenadeId, true)
                .map(grenadeMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Grenade with id '%d' not found".formatted(grenadeId)));
    }

    @Override
    public GrenadeInfo createGrenade(CreateGrenadeRequest request) {
        if (!request.hasData()) {
            throw new InvalidRequestException("Data must not be empty");
        }

        validateGrenadeWritableData(request.getData());

        Grenade grenade = grenadeMapper.toEntity(request.getData());
        grenade = grenadeRepository.save(grenade);

        return grenadeMapper.toResponse(grenade);
    }

    private void validateGrenadeWritableData(GrenadeWritableData data) {
        if (StringUtils.isBlank(data.getName())) {
            throw new InvalidRequestException("Name cannot be blank or null");
        }

        if (data.getBlastDamageRadiusMeters() < 0) {
            throw new InvalidRequestException("Blast damage radius cannot be less than 0");
        }

        if (data.getMaxBlastDamageHP() < 0) {
            throw new InvalidRequestException("Max blast damage hp cannot be less than 0");
        }

        if (data.getMaxBlindTimeMs() < 0) {
            throw new InvalidRequestException("Max blind time ms cannot be less than 0");
        }

        if (data.getMaxDeafTimeMs() < 0) {
            throw new InvalidRequestException("Max deaf time ms cannot be less than 0");
        }
    }

    @Override
    public GrenadeInfo updateGrenade(UpdateGrenadeRequest request) {
        Grenade grenade = grenadeRepository.findById(request.getGrenadeId())
                .orElseThrow(() -> new ObjectNotFoundException("Grenade with id '%d' not found".formatted(request.getGrenadeId())));

        if (!request.hasData()) {
            return grenadeMapper.toResponse(grenade);
        }

        validateGrenadeWritableData(request.getData());

        grenadeMapper.updateGrenade(grenade, request.getData());
        grenade = grenadeRepository.save(grenade);

        return grenadeMapper.toResponse(grenade);
    }

    @Override
    public GrenadeInfoListPage getGrenades(GetGrenadesRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<Grenade> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PaginationUtils.getPageable(request.getPaginationRequest(), Sort.by(Sort.Direction.ASC, Grenade.Fields.id)) :
                PaginationUtils.getPageable(0, 10, Sort.by(Sort.Direction.ASC, Grenade.Fields.id));

        Page<Grenade> data = grenadeRepository.findAll(specification, pageable);

        return GrenadeInfoListPage.newBuilder()
                .addAllData(data.map(grenadeMapper::toResponse))
                .setTotalCount(data.getTotalElements())
                .build();
    }

    private static Specification<Grenade> getSpecification(GrenadesFilter filter) {
        List<Specification<Grenade>> specifications = new ArrayList<>();

        if (filter.hasEnabled()) {
            specifications.add(GrenadeSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.hasName()) {
            specifications.add(GrenadeSpecifications.byNameStartsWith(filter.getName()));
        }

        if (filter.getGrenadeIdCount() > 0) {
            specifications.add(GrenadeSpecifications.byGrenadeIds(filter.getGrenadeIdList()));
        }

        return Specification.allOf(specifications);
    }
}
