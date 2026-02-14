package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.PlayerCurrencyOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ProductCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccount;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.outbox.ProductTradeIssueRequiredMessage;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.ProductTradeIssueRequiredMessageMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.ProductTradeMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProductTradeIssueRequiredMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProductTradeRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.ProductTradeSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.util.TransactionExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ProductTradeServiceImpl implements ProductTradeService {

    private final ProductCacheService productCacheService;
    private final PlayerAccountService playerAccountService;
    private final ProductTradeRepository productTradeRepository;
    private final PlayerAccountRepository playerAccountRepository;
    private final ProductTradeMapper productTradeMapper;
    private final ProductTradeIssueRequiredMessageMapper productTradeIssueRequiredMessageMapper;
    private final ProductTradeIssueRequiredMessageRepository productTradeIssueRequiredMessageRepository;
    private final TransactionExecutor transactionExecutor;

    @Override
    public ProductTrade createProductTrade(@NotNull UUID playerUuid, int productId) {
        Product product = productCacheService.getById(productId)
                .filter(Product::isEnabled)
                .orElseThrow(() -> new ObjectNotFoundException("Enabled product with id %d not found"
                        .formatted(productId)));

        if (product.getEquipment() == null || !product.getEquipment().isEnabled()) {
            throw new InvalidRequestException("Product equipment is not found or not enabled");
        }

        if (product.getPriceCurrency() == null || !product.getPriceCurrency().isEnabled()) {
            throw new InvalidRequestException("Product price currency is not found or not enabled");
        }

        PlayerAccount playerAccount = playerAccountRepository.findByPlayerUuid(playerUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Player with uuid %s not found".formatted(playerUuid)));

        ProductTrade productTrade = productTradeMapper.toEntity(playerAccount.getPlayerUuid(), product);

        ProductTradeIssueRequiredMessage productTradeIssueRequiredMessage = productTradeIssueRequiredMessageMapper.toMessage(productTrade);

        return transactionExecutor.execute(() -> {
            ProductTrade savedProductTrade = productTradeRepository.save(productTrade);

            playerAccountService.performCurrencyWriteOff(
                    savedProductTrade.getUuid(),
                    PlayerCurrencyOperationCommand.builder()
                            .playerUuid(playerAccount.getPlayerUuid())
                            .amount(product.getPrice())
                            .currencyId(product.getPriceCurrency().getId())
                            .build()
            );

            productTradeIssueRequiredMessageRepository.save(productTradeIssueRequiredMessage);

            return savedProductTrade;
        });
    }

    @Override
    public ProductTrade getProductTrade(@NotNull UUID playerUuid, @NotNull UUID tradeUuid) {
        return productTradeRepository.findByPlayerUuidAndUuid(playerUuid, tradeUuid)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Product trade with uuid '%s' and player uuid '%s' not found".formatted(tradeUuid, playerUuid))
                );
    }

    @Override
    public Page<ProductTrade> getProductTrades(@NotNull @Valid ProductTradeFilterParams filterParams,
                                               @NotNull Pageable pageable
    ) {
        Specification<ProductTrade> specification = getSpecification(filterParams);
        return productTradeRepository.findAll(specification, pageable);
    }

    private Specification<ProductTrade> getSpecification(ProductTradeFilterParams filter) {
        List<Specification<ProductTrade>> specifications = new ArrayList<>();

        if (filter.getPlayerUuid() != null) {
            specifications.add(ProductTradeSpecifications.byPlayerUuid(filter.getPlayerUuid()));
        }

        if (!filter.getProductIds().isEmpty()) {
            specifications.add(ProductTradeSpecifications.byProductIds(filter.getProductIds()));
        }

        if (filter.getEquipmentType() != null && !filter.getEquipmentIds().isEmpty()) {
            specifications.add(ProductTradeSpecifications.byEquipmentTypeAndEquipmentIds(
                    filter.getEquipmentType(),
                    filter.getEquipmentIds()
            ));
        }

        if (!filter.getStatuses().isEmpty()) {
            specifications.add(ProductTradeSpecifications.byStatuses(filter.getStatuses()));
        }

        return Specification.allOf(specifications);
    }
}