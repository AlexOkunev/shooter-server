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
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectAlreadyExistsException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.PlayerCurrencyOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccount;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.PlayerAccountItemMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.InitialPlayerAccountItemRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountItemRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.PlayerAccountItemSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class PlayerAccountServiceImpl implements PlayerAccountService {

    private final PlayerAccountRepository playerAccountRepository;
    private final PlayerAccountItemRepository playerAccountItemRepository;
    private final PlayerAccountItemMapper playerAccountItemMapper;
    private final InitialPlayerAccountItemRepository initialPlayerAccountItemRepository;
    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;

    @Override
    public Page<PlayerAccountItem> getPlayerAccountItems(@NotNull UUID playerUuid, boolean onlyEnabledCurrencies, @NotNull Pageable pageable) {
        PlayerAccount playerAccount = getPlayerAccount(playerUuid);

        Specification<PlayerAccountItem> specification = getSpecification(
                playerAccount.getPlayerUuid(),
                onlyEnabledCurrencies
        );

        return playerAccountItemRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public void initializePlayerAccount(@NotNull UUID playerUuid) {
        playerAccountRepository.findByPlayerUuid(playerUuid).ifPresent(playerAccount -> {
            throw new ObjectAlreadyExistsException("Player %s account is already initialized".formatted(playerUuid));
        });

        List<PlayerAccountItem> inventoryItems = initialPlayerAccountItemRepository.findAllByEnabledIsTrue().stream()
                .map(initialItem -> playerAccountItemMapper.toEntity(initialItem, playerUuid))
                .toList();

        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setPlayerUuid(playerUuid);
        playerAccountRepository.save(playerAccount);

        playerAccountItemRepository.saveAll(inventoryItems);
    }

    @Override
    @Transactional
    public void initializePlayerAccountBySystemEvent(@NotNull UUID playerUuid) {
        Optional<PlayerAccount> foundPlayerAccount = playerAccountRepository.findByPlayerUuid(playerUuid);

        if (foundPlayerAccount.isPresent()) {
            log.error("Player {} account is already initialized", playerUuid);
            return;
        }

        List<PlayerAccountItem> inventoryItems = initialPlayerAccountItemRepository.findAllByEnabledIsTrue().stream()
                .map(initialItem -> playerAccountItemMapper.toEntity(initialItem, playerUuid))
                .toList();

        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setPlayerUuid(playerUuid);
        playerAccountRepository.save(playerAccount);

        playerAccountItemRepository.saveAll(inventoryItems);
    }

    @Override
    public void setPlayerAccountEmailBySystemEvent(UUID playerUuid, String email) {
        Optional<PlayerAccount> foundPlayerAccount = playerAccountRepository.findByPlayerUuid(playerUuid);

        if (foundPlayerAccount.isEmpty()) {
            log.error("Player {} not found", playerUuid);
            return;
        }

        PlayerAccount playerAccount = foundPlayerAccount.get();
        playerAccount.setEmail(email);
        playerAccountRepository.save(playerAccount);
    }

    @Override
    @Transactional
    public PlayerAccountItem giveCurrency(@Valid @NotNull PlayerCurrencyOperationCommand command) {
        PlayerAccount playerAccount = getPlayerAccount(command.getPlayerUuid());
        ReferenceCurrency currency = getReferenceCurrency(command);

        PlayerAccountItem playerAccountItem = playerAccountItemRepository.findByPlayerUuidAndCurrencyId(playerAccount.getPlayerUuid(), currency.getId())
                .orElse(playerAccountItemMapper.toEntityWithZeroAmount(playerAccount.getPlayerUuid(), currency.getId()));

        playerAccountItem.setAmount(playerAccountItem.getAmount() + command.getAmount());

        return playerAccountItemRepository.save(playerAccountItem);
    }

    @Override
    @Transactional
    public PlayerAccountItem takeAwayCurrency(@Valid @NotNull PlayerCurrencyOperationCommand command) {
        PlayerAccount playerAccount = getPlayerAccount(command.getPlayerUuid());
        ReferenceCurrency currency = getReferenceCurrency(command);

        PlayerAccountItem playerAccountItem = playerAccountItemRepository.findByPlayerUuidAndCurrencyId(playerAccount.getPlayerUuid(), currency.getId())
                .orElseThrow(() -> new InvalidRequestException("Insufficient amount of currency"));

        if (playerAccountItem.getAmount() < command.getAmount()) {
            throw new InvalidRequestException("Insufficient amount of currency");
        }

        playerAccountItem.setAmount(playerAccountItem.getAmount() - command.getAmount());

        return playerAccountItemRepository.save(playerAccountItem);
    }

    @Override
    @Transactional
    public void performCurrencyWriteOff(@Valid @NotNull PlayerCurrencyOperationCommand command) {
        PlayerAccount playerAccount = getPlayerAccount(command.getPlayerUuid());
        ReferenceCurrency currency = getReferenceCurrency(command);

        if (!currency.isEnabled()) {
            throw new InvalidRequestException("Currency is disabled");
        }

        PlayerAccountItem playerAccountItem = playerAccountItemRepository.findByPlayerUuidAndCurrencyId(playerAccount.getPlayerUuid(), currency.getId())
                .orElseThrow(() -> new InvalidRequestException("Insufficient amount of currency"));

        if (playerAccountItem.getAmount() < command.getAmount()) {
            throw new InvalidRequestException("Insufficient amount of currency");
        }

        playerAccountItem.setAmount(playerAccountItem.getAmount() - command.getAmount());

        playerAccountItemRepository.save(playerAccountItem);
    }

    private PlayerAccount getPlayerAccount(UUID playerUuid) {
        return playerAccountRepository.findByPlayerUuid(playerUuid).orElseThrow(
                () -> new ObjectNotFoundException("Player %s account is not found".formatted(playerUuid)));
    }

    private ReferenceCurrency getReferenceCurrency(PlayerCurrencyOperationCommand command) {
        return referenceCurrencyCacheService.getById(command.getCurrencyId())
                .orElseThrow(() -> new IllegalArgumentException("Currency with ID %d not found".formatted(command.getCurrencyId())));
    }

    private Specification<PlayerAccountItem> getSpecification(UUID playerUuid, boolean onlyEnabledCurrencies) {
        List<Specification<PlayerAccountItem>> specifications = new ArrayList<>();

        specifications.add(PlayerAccountItemSpecifications.byPlayerUuid(playerUuid));
        specifications.add(PlayerAccountItemSpecifications.byPositiveAmount());

        if (onlyEnabledCurrencies) {
            specifications.add(PlayerAccountItemSpecifications.byEnabledCurrency());
        }

        return Specification.allOf(specifications);
    }
}
