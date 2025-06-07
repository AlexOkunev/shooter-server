package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ProductCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccount;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.ProductEquipmentTypeMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.ProductTradeMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.ProductTradeStatusMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProductTradeRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.ProductTradeSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTradeServiceImpl implements ProductTradeService {
    private final PaginationInfoMapper paginationInfoMapper;

    private final ProductCacheService productCacheService;

    private final ProductTradeRepository productTradeRepository;

    private final PlayerAccountRepository playerAccountRepository;

    private final ProductTradeMapper productTradeMapper;

    private final ProductEquipmentTypeMapper productEquipmentTypeMapper;

    private final ProductTradeStatusMapper productTradeStatusMapper;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, ProductTrade.Fields.id);

    @Override
    public ProductTradeInfo createProductTrade(CreateProductTradeRequest request) {
        Product product = productCacheService.getById(request.getProductId())
                .filter(Product::isEnabled)
                .orElseThrow(() -> new ObjectNotFoundException("Enabled product with id %d not found".formatted(request.getProductId())));

        if (product.getEquipment() == null || !product.getEquipment().isEnabled()) {
            throw new InvalidRequestException("Product equipment is not enabled");
        }

        PlayerAccount playerAccount = playerAccountRepository.findByPlayerId(request.getPlayerId())
                .orElseThrow(() -> new ObjectNotFoundException("Player with id %d not found".formatted(request.getPlayerId())));

        ProductTrade productTrade = productTradeMapper.toEntity(playerAccount.getPlayerId(), product);
        productTrade = productTradeRepository.save(productTrade);

        return productTradeMapper.toResponse(productTrade);
    }

    @Override
    public ProductTradeInfo getProductTrade(GetProductTradeRequest request) {
        return productTradeRepository.findByIdAndPlayerId(request.getTradeId(), request.getPlayerId())
                .map(productTradeMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Product trade with id '%d' and player id '%d' not found".formatted(request.getTradeId(), request.getPlayerId())));
    }

    @Override
    public ProductTradeInfoListPage getProductTrades(GetProductTradesRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<ProductTrade> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Page<ProductTrade> data = productTradeRepository.findAll(specification, pageable);

        return ProductTradeInfoListPage.newBuilder()
                .addAllData(data.map(productTradeMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    private Specification<ProductTrade> getSpecification(GetProductTradesRequest.Filter filter) {
        List<Specification<ProductTrade>> specifications = new ArrayList<>();

        if (filter.hasPlayerId()) {
            specifications.add(ProductTradeSpecifications.byPlayerId(filter.getPlayerId()));
        }

        if (filter.getTradeIdsCount() > 0) {
            specifications.add(ProductTradeSpecifications.byIds(filter.getTradeIdsList()));
        }

        if (filter.getProductIdsCount() > 0) {
            specifications.add(ProductTradeSpecifications.byProductIds(filter.getProductIdsList()));
        }

        if (filter.hasEquipmentType() && filter.getEquipmentIdsCount() > 0) {
            specifications.add(ProductTradeSpecifications.byEquipmentTypeAndEquipmentIds(
                    productEquipmentTypeMapper.toEntity(filter.getEquipmentType()),
                    filter.getEquipmentIdsList())
            );
        }

        if (filter.getStatusesCount() > 0) {
            List<ProductTradeStatus> statues = productTradeStatusMapper.toEntityList(filter.getStatusesList());
            specifications.add(ProductTradeSpecifications.byStatuses(statues));
        }

        return Specification.allOf(specifications);
    }
}