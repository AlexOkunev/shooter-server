package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectAlreadyExistsException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccount;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PlayerAccountItemMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.InitialPlayerAccountItemRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountItemRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.PlayerAccountItemSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerAccountServiceImpl implements PlayerAccountService {
    private final PlayerAccountRepository playerAccountRepository;

    private final PlayerAccountItemRepository playerAccountItemRepository;

    private final PlayerAccountItemMapper playerAccountItemMapper;

    private final PaginationInfoMapper paginationInfoMapper;

    private final InitialPlayerAccountItemRepository initialPlayerAccountItemRepository;

    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.asc(PlayerAccountItem.Fields.currencyId));

    @Override
    public PlayerAccountItemsPage getPlayerAccount(GetPlayerAccountRequest request) {
        playerAccountRepository.findById(request.getPlayerId()).orElseThrow(() ->
                new ObjectNotFoundException("Player %d account not found".formatted(request.getPlayerId())));

        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Specification<PlayerAccountItem> specification = getSpecification(request);

        Page<PlayerAccountItem> data = playerAccountItemRepository.findAll(specification, pageable);

        return PlayerAccountItemsPage.newBuilder()
                .addAllData(data.map(playerAccountItemMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    @Override
    @Transactional
    public void initializePlayerAccount(InitializePlayerAccountRequest request) {
        playerAccountRepository.findById(request.getPlayerId()).ifPresent(playerAccount -> {
            throw new ObjectAlreadyExistsException("Player %d account is already initialized".formatted(playerAccount.getPlayerId()));
        });

        List<PlayerAccountItem> inventoryItems = initialPlayerAccountItemRepository.findAll().stream()
                .map(initialItem -> playerAccountItemMapper.toEntity(initialItem, request.getPlayerId()))
                .toList();

        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setPlayerId(request.getPlayerId());
        playerAccountRepository.save(playerAccount);

        playerAccountItemRepository.saveAll(inventoryItems);
    }

    @Override
    public PlayerAccountItemInfo giveCurrency(PlayerCurrencyOperationRequest request) {
        validateCurrencyAmountPositivity(request);

        ReferenceCurrency currency = referenceCurrencyCacheService.getById(request.getCurrencyId())
                .orElseThrow(() -> new IllegalArgumentException("Currency with ID %d not found".formatted(request.getCurrencyId())));

        PlayerAccount playerAccount = playerAccountRepository.findById(request.getPlayerId()).orElseThrow(
                () -> new ObjectNotFoundException("Player %d account is not found".formatted(request.getPlayerId())));

        PlayerAccountItem playerAccountItem = playerAccountItemRepository.findByPlayerIdAndCurrencyId(playerAccount.getPlayerId(), currency.getId())
                .orElse(playerAccountItemMapper.toEntityWithZeroAmount(currency, playerAccount.getPlayerId()));

        playerAccountItem.setCurrencyAmount(playerAccountItem.getCurrencyAmount() + request.getAmount());
        playerAccountItem = playerAccountItemRepository.save(playerAccountItem);

        return playerAccountItemMapper.toResponse(playerAccountItem);
    }

    @Override
    public PlayerAccountItemInfo takeAwayCurrency(PlayerCurrencyOperationRequest request) {
        validateCurrencyAmountPositivity(request);

        ReferenceCurrency currency = referenceCurrencyCacheService.getById(request.getCurrencyId())
                .orElseThrow(() -> new IllegalArgumentException("Currency with ID %d not found".formatted(request.getCurrencyId())));

        PlayerAccount playerAccount = playerAccountRepository.findById(request.getPlayerId()).orElseThrow(
                () -> new ObjectNotFoundException("Player %d account is not found".formatted(request.getPlayerId())));

        PlayerAccountItem playerAccountItem = playerAccountItemRepository.findByPlayerIdAndCurrencyId(playerAccount.getPlayerId(), currency.getId())
                .orElse(playerAccountItemMapper.toEntityWithZeroAmount(currency, playerAccount.getPlayerId()));

        if (playerAccountItem.getCurrencyAmount() < request.getAmount()) {
            throw new InvalidRequestException("Insufficient amount of currency");
        }

        playerAccountItem.setCurrencyAmount(playerAccountItem.getCurrencyAmount() - request.getAmount());
        playerAccountItem = playerAccountItemRepository.save(playerAccountItem);

        return playerAccountItemMapper.toResponse(playerAccountItem);
    }

    @Override
    public void performCurrencyWriteOff(PlayerAccount playerAccount, ReferenceCurrency currency, int price) {
        if (price < 0) {
            throw new InvalidRequestException("Price cannot be negative");
        }

        PlayerAccountItem playerAccountItem = playerAccountItemRepository.findByPlayerIdAndCurrencyId(playerAccount.getPlayerId(), currency.getId())
                .orElse(playerAccountItemMapper.toEntityWithZeroAmount(currency, playerAccount.getPlayerId()));

        if (playerAccountItem.getCurrencyAmount() < price) {
            throw new InvalidRequestException("Insufficient amount of currency");
        }

        playerAccountItem.setCurrencyAmount(playerAccountItem.getCurrencyAmount() - price);
        playerAccountItemRepository.save(playerAccountItem);
    }

    private static void validateCurrencyAmountPositivity(PlayerCurrencyOperationRequest request) {
        if (request.getAmount() <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero");
        }
    }

    private Specification<PlayerAccountItem> getSpecification(GetPlayerAccountRequest request) {
        return Specification.allOf(
                PlayerAccountItemSpecifications.byPlayerId(request.getPlayerId()),
                PlayerAccountItemSpecifications.byPositiveAmount()
        );
    }
}
