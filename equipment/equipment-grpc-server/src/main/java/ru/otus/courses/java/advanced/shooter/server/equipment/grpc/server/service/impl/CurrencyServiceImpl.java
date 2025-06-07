package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.CurrencyMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.CurrencyRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.CurrencyService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.CurrencySpecifications;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    private final PaginationInfoMapper paginationInfoMapper;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, Currency.Fields.id);

    @Override
    public CurrencyInfo getCurrencyInfo(int currencyId) {
        return currencyRepository.findById(currencyId)
                .map(currencyMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id '%d' not found".formatted(currencyId)));
    }

    @Override
    public CurrencyInfo getEnabledCurrencyInfo(int currencyId) {
        return currencyRepository.findByIdAndEnabled(currencyId, true)
                .map(currencyMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id '%d' not found".formatted(currencyId)));
    }

    @Override
    public CurrencyInfo createCurrency(CreateCurrencyRequest request) {
        if (!request.hasData()) {
            throw new InvalidRequestException("Data must not be empty");
        }

        validateCurrencyWritableData(request.getData());

        Currency currency = currencyMapper.toEntity(request.getData());
        currency = currencyRepository.save(currency);

        return currencyMapper.toResponse(currency);
    }

    private void validateCurrencyWritableData(CurrencyWritableData data) {
        if (StringUtils.isBlank(data.getName())) {
            throw new InvalidRequestException("Name cannot be blank or null");
        }
    }

    @Override
    public CurrencyInfo updateCurrency(UpdateCurrencyRequest request) {
        Currency currency = currencyRepository.findById(request.getCurrencyId())
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id '%d' not found".formatted(request.getCurrencyId())));

        if (!request.hasData()) {
            return currencyMapper.toResponse(currency);
        }

        validateCurrencyWritableData(request.getData());

        currencyMapper.updateCurrency(currency, request.getData());
        currency = currencyRepository.save(currency);

        return currencyMapper.toResponse(currency);
    }

    @Override
    public CurrencyInfoListPage getCurrencies(GetCurrenciesRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<Currency> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Page<Currency> data = currencyRepository.findAll(specification, pageable);

        return CurrencyInfoListPage.newBuilder()
                .addAllData(data.map(currencyMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    private static Specification<Currency> getSpecification(CurrenciesFilter filter) {
        List<Specification<Currency>> specifications = new ArrayList<>();

        if (filter.hasEnabled()) {
            specifications.add(CurrencySpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.hasName()) {
            specifications.add(CurrencySpecifications.byNameStartsWith(filter.getName()));
        }

        if (filter.hasCanBeBought()) {
            specifications.add(CurrencySpecifications.byCanBeBought(filter.getCanBeBought()));
        }

        if (filter.hasCanBeGivenAsAward()) {
            specifications.add(CurrencySpecifications.byCanBeGivenAsAward(filter.getCanBeGivenAsAward()));
        }

        if (filter.getCurrencyIdCount() > 0) {
            specifications.add(CurrencySpecifications.byCurrencyIds(filter.getCurrencyIdList()));
        }

        return Specification.allOf(specifications);
    }
}
