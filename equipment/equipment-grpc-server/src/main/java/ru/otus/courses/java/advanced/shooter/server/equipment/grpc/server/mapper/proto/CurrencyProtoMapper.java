package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencyFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencySavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class CurrencyProtoMapper {
    private static final class ProtoFields {
        static final String NAME = "name";
        static final String CURRENCY_ID = "currencyId";
    }

    public abstract CurrencyInfo toResponse(Currency source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CurrencyInfo> toResponseList(Iterable<Currency> source);

    public CurrencySavedData toSavedData(CreateCurrencyRequest createCurrencyRequest) {
        if (!createCurrencyRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(createCurrencyRequest.getData());
    }

    public CurrencySavedData toSavedData(UpdateCurrencyRequest updateCurrencyRequest) {
        if (!updateCurrencyRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(updateCurrencyRequest.getData());
    }

    public CurrencyFilterParams toFilterParams(GetCurrenciesRequest getCurrenciesRequest) {
        if (!getCurrenciesRequest.hasFilter()) {
            return CurrencyFilterParams.builder().build();
        }

        return toFilterParamsInternal(getCurrenciesRequest.getFilter());
    }

    public Pageable toPageable(GetCurrenciesRequest getCurrenciesRequest) {
        if (!getCurrenciesRequest.hasPaginationRequest()) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"));
        }

        return PageRequest.of(
                getCurrenciesRequest.getPaginationRequest().getPage(),
                getCurrenciesRequest.getPaginationRequest().getCount(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    @Mapping(
            target = Currency.Fields.name,
            source = ProtoFields.NAME,
            qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
    )
    protected abstract CurrencySavedData toSavedDataInternal(CurrencyWritableData source);

    @Mapping(
            target = CurrencyFilterParams.Fields.currencyIds,
            source = ProtoFields.CURRENCY_ID
    )
    protected abstract CurrencyFilterParams toFilterParamsInternal(CurrenciesFilter filter);
}