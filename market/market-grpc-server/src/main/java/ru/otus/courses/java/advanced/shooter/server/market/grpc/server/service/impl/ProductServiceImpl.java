package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.event.ProductChangedEvent;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.ProductEquipmentTypeMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.ProductMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProductRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.ProductSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.util.TransactionExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    private final ProductRepository productRepository;

    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;

    private final ReferenceEquipmentCacheService referenceEquipmentCacheService;

    private final PaginationInfoMapper paginationInfoMapper;

    private final ProductEquipmentTypeMapper productEquipmentTypeMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TransactionExecutor transactionExecutor;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, Product.Fields.id);

    @Override
    public ProductInfo getProduct(GetProductRequest request) {
        return productRepository.findById(request.getId())
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id '%d' not found".formatted(request.getId())));
    }

    @Override
    public ProductInfo getEnabledProduct(GetProductRequest request) {
        return productRepository.findByIdAndEnabledIsTrue(request.getId())
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id '%d' not found".formatted(request.getId())));
    }

    @Override
    public ProductInfoListPage getProducts(GetProductsRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<Product> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Page<Product> data = productRepository.findAll(specification, pageable);

        return ProductInfoListPage.newBuilder()
                .addAllData(data.map(productMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    @Override
    public ProductInfo createProduct(CreateProductRequest request) {
        if (!request.hasData()) {
            throw new InvalidRequestException("Data must not be empty");
        }

        validateProductWritableData(request.getData());

        int priceCurrencyId = request.getData().getPrice().getCurrencyId();

        ReferenceCurrency currency = referenceCurrencyCacheService.getById(priceCurrencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id %d not found".formatted(priceCurrencyId)));

        int equipmentId = request.getData().getEquipment().getId();
        ProductEquipmentType equipmentType = productEquipmentTypeMapper.toEntity(request.getData().getEquipment().getType());

        ReferenceEquipment equipment = referenceEquipmentCacheService.getById(new ReferenceEquipmentId(equipmentId, equipmentType))
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Equipment %s with id %d not found".formatted(equipmentType.name(), equipmentId)));

        Product product = productMapper.toEntity(request.getData(), currency, equipment);
        product = productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Override
    public ProductInfo updateProduct(UpdateProductRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ObjectNotFoundException("Product with id '%d' not found".formatted(request.getProductId())));

        if (product.getVersion() != request.getVersion()) {
            throw new InvalidRequestException("Product version does not match product version");
        }

        if (!request.hasData()) {
            return productMapper.toResponse(product);
        }

        validateProductWritableData(request.getData());

        int priceCurrencyId = request.getData().getPrice().getCurrencyId();

        ReferenceCurrency currency = referenceCurrencyCacheService.getById(priceCurrencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id %d not found".formatted(priceCurrencyId)));

        int equipmentId = request.getData().getEquipment().getId();
        ProductEquipmentType equipmentType = productEquipmentTypeMapper.toEntity(request.getData().getEquipment().getType());

        ReferenceEquipment equipment = referenceEquipmentCacheService.getById(new ReferenceEquipmentId(equipmentId, equipmentType))
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Equipment %s with id %d not found".formatted(equipmentType.name(), equipmentId)));

        productMapper.update(product, request.getData(), currency, equipment);

        Product savedProduct = transactionExecutor.execute(() -> {
            Product temp = productRepository.save(product);
            applicationEventPublisher.publishEvent(new ProductChangedEvent(product.getId()));
            return temp;
        });

        return productMapper.toResponse(savedProduct);
    }

    private void validateProductWritableData(ProductWritableData data) {
        if (!data.hasEquipment()) {
            throw new InvalidRequestException("Data must contain equipment");
        }

        if (data.getEquipment().getType() == ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductEquipmentType.UNRECOGNIZED) {
            throw new InvalidRequestException("Unrecognized equipment type");
        }

        if (data.getEquipment().getAmount() <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero");
        }

        if (!data.hasPrice()) {
            throw new InvalidRequestException("Data must contain price");
        }

        if (data.getPrice().getAmount() <= 0) {
            throw new InvalidRequestException("Price must be greater than zero");
        }
    }

    private Specification<Product> getSpecification(GetProductsRequest.Filter filter) {
        List<Specification<Product>> specifications = new ArrayList<>();

        if (filter.getIdsCount() > 0) {
            specifications.add(ProductSpecifications.byIds(filter.getIdsList()));
        }

        if (filter.hasEnabled()) {
            specifications.add(ProductSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.getEquipmentTypesCount() > 0) {
            List<ProductEquipmentType> equipmentTypes = filter.getEquipmentTypesList().stream()
                    .map(productEquipmentTypeMapper::toEntity)
                    .toList();
            specifications.add(ProductSpecifications.byEquipmentTypes(equipmentTypes));
        }

        if (filter.getEquipmentIdsCount() > 0) {
            specifications.add(ProductSpecifications.byEquipmentIds(filter.getEquipmentIdsList()));
        }

        if (filter.hasEquipmentEnabled()) {
            specifications.add(ProductSpecifications.byEquipmentEnabled(filter.getEquipmentEnabled()));
        }

        if (filter.getPriceCurrencyIdsCount() > 0) {
            specifications.add(ProductSpecifications.byPriceCurrencyIds(filter.getPriceCurrencyIdsList()));
        }

        if (filter.hasPriceCurrencyEnabled()) {
            specifications.add(ProductSpecifications.byPriceCurrencyEnabled(filter.getPriceCurrencyEnabled()));
        }

        if (filter.hasPriceLow()) {
            specifications.add(ProductSpecifications.byPriceGte(filter.getPriceLow()));
        }

        if (filter.hasPriceHigh()) {
            specifications.add(ProductSpecifications.byPriceLte(filter.getPriceHigh()));
        }

        if (filter.hasEquipmentAmountLow()) {
            specifications.add(ProductSpecifications.byEquipmentAmountGte(filter.getEquipmentAmountLow()));
        }

        if (filter.hasEquipmentAmountHigh()) {
            specifications.add(ProductSpecifications.byEquipmentAmountLte(filter.getEquipmentAmountHigh()));
        }

        return Specification.allOf(specifications);
    }
}