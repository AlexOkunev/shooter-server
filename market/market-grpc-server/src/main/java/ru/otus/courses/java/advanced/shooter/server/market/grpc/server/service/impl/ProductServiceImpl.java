package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.ProductMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProductRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.ProductSpecifications;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;
    private final ReferenceEquipmentCacheService referenceEquipmentCacheService;

    @Override
    public Product getProduct(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id '%d' not found".formatted(id)));
    }

    @Override
    public Product getEnabledProduct(int id) {
        return productRepository.findByIdAndEnabledIsTrue(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id '%d' not found".formatted(id)));
    }

    @Override
    @Transactional
    public Product createProduct(@Valid @NotNull ProductSavedData data) {
        validateRelatedEntities(
                data.getPriceCurrencyId(),
                data.getEquipmentType(),
                data.getEquipmentId()
        );

        Product product = productMapper.toEntity(data);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(
            int id,
            int version,
            @Valid @NotNull ProductSavedData data
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id '%d' not found".formatted(id)));

        if (product.getVersion() != version) {
            throw new InvalidRequestException("Product version does not match product version");
        }

        validateRelatedEntities(
                data.getPriceCurrencyId(),
                data.getEquipmentType(),
                data.getEquipmentId()
        );

        productMapper.update(product, data);

        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProducts(
            @NotNull ProductFilterParams filterParams,
            @NotNull Pageable pageable
    ) {
        Specification<Product> specification = getSpecification(filterParams);
        return productRepository.findAll(specification, pageable);
    }

    private void validateRelatedEntities(int priceCurrencyId, ProductEquipmentType equipmentType, int equipmentId) {
        ReferenceCurrency currency = referenceCurrencyCacheService.getById(priceCurrencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id %d not found".formatted(priceCurrencyId)));

        if (!currency.isEnabled()) {
            throw new InvalidRequestException("Currency is disabled");
        }

        ReferenceEquipment equipment = referenceEquipmentCacheService.getById(new ReferenceEquipmentId(equipmentType, equipmentId))
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Equipment %s with id %d not found".formatted(equipmentType.name(), equipmentId)));

        if (!equipment.isEnabled()) {
            throw new InvalidRequestException("Equipment is disabled");
        }
    }

    private Specification<Product> getSpecification(ProductFilterParams filter) {
        List<Specification<Product>> specifications = new ArrayList<>();

        if (filter.getEnabled() != null) {
            specifications.add(ProductSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.getPriceCurrencyEnabled() != null) {
            specifications.add(ProductSpecifications.byPriceCurrencyEnabled(filter.getPriceCurrencyEnabled()));
        }

        if (!filter.getEquipmentTypes().isEmpty()) {
            specifications.add(ProductSpecifications.byEquipmentTypes(filter.getEquipmentTypes()));
        }

        if (filter.getEquipmentEnabled() != null) {
            specifications.add(ProductSpecifications.byEquipmentEnabled(filter.getEquipmentEnabled()));
        }

        if (!filter.getPriceCurrencyIds().isEmpty()) {
            specifications.add(ProductSpecifications.byPriceCurrencyIds(filter.getPriceCurrencyIds()));
        }

        return Specification.allOf(specifications);
    }
}