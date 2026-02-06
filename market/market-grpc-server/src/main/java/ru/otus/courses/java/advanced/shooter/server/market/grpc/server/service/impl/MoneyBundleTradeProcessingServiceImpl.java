package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.PlayerCurrencyOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProcessedExternalMessage;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.exception.AbsentPaymentException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.exception.InvalidMoneyBundleTradeStatusException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleTradeRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProcessedExternalMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleTradeProcessingService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.payment.common.PaymentStatusCodes;
import ru.otus.courses.java.advanced.shooter.server.payment.outbox.ProcessedPaymentMessage;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoneyBundleTradeProcessingServiceImpl implements MoneyBundleTradeProcessingService {

    private final MoneyBundleTradeRepository moneyBundleTradeRepository;
    private final ProcessedExternalMessageRepository processedExternalMessageRepository;
    private final PlayerAccountService playerAccountService;

    @Override
    @Transactional
    public void processMessage(ProcessedPaymentMessage message) {
        log.info("Process payment processed message {}. Player uuid {}. Trade uuid {}", message.getUuid(), message.getPlayerUuid(), message.getTradeUuid());

        UUID messageUuid = UUID.fromString(message.getUuid());

        //TODO использовать кеш сообщений. но не загружать в него старые сообщения, только те которые были явно запрошены. кеш чистить
        if (processedExternalMessageRepository.existsByMessageUUID(messageUuid)) {
            log.error("External message with uuid {} has already been processed", message.getUuid());
            return;
        }

        completeMoneyBundleTrade(
                UUID.fromString(message.getPlayerUuid()),
                UUID.fromString(message.getTradeUuid()),
                ZonedDateTime.ofInstant(message.getProcessingFinishedTimestamp(), ZoneOffset.UTC),
                message.getStatus()
        );

        ProcessedExternalMessage processedExternalMessage = new ProcessedExternalMessage();
        processedExternalMessage.setMessageUUID(messageUuid);
        processedExternalMessage.setMessageCreatedTimestamp(ZonedDateTime.ofInstant(message.getCreatedTimestamp(), ZoneOffset.UTC));
        processedExternalMessage.setMessageProcessedTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        processedExternalMessage = processedExternalMessageRepository.save(processedExternalMessage);

        log.info("Save processed external message: {}", processedExternalMessage.getMessageUUID());
    }

    private void completeMoneyBundleTrade(UUID playerUuid, UUID tradeUuid, ZonedDateTime paymentFinishTimestamp, int paymentStatusCode) {
        if (paymentStatusCode != PaymentStatusCodes.SUCCESS_CODE && paymentStatusCode != PaymentStatusCodes.FAILED_CODE) {
            log.error("Money bundle trade {} processed message contains incorrect payment status code {}", tradeUuid, paymentStatusCode);
            throw new IllegalArgumentException("Money bundle trade %s processed message contains incorrect payment status code %d"
                    .formatted(tradeUuid, paymentStatusCode));
        }

        MoneyBundleTrade moneyBundleTrade = moneyBundleTradeRepository.findByPlayerUuidAndUuid(playerUuid, tradeUuid)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Player %s money bundle trade %s not found".formatted(playerUuid, tradeUuid)
                ));

        if (moneyBundleTrade.getStatus() != MoneyBundleTradeStatus.PAYMENT_PENDING) {
            log.error("Money bundle trade with uuid {} has incorrect status {}. Must be {}",
                    tradeUuid, moneyBundleTrade.getStatus(), MoneyBundleTradeStatus.PAYMENT_PENDING);
            throw new InvalidMoneyBundleTradeStatusException("Money bundle trade %s has incorrect status %s. Must be %s"
                    .formatted(tradeUuid, moneyBundleTrade.getStatus(), MoneyBundleTradeStatus.PAYMENT_PENDING));
        }

        if (moneyBundleTrade.getPayment() == null) {
            log.error("Payment must not be null for player {} money bundle trade with uuid {}",
                    moneyBundleTrade.getPlayerUuid(), moneyBundleTrade.getUuid());
            throw new AbsentPaymentException("Payment is absent for player %s money bundle trade %s"
                    .formatted(moneyBundleTrade.getPlayerUuid(), moneyBundleTrade.getUuid()));
        }

        if (paymentStatusCode == PaymentStatusCodes.SUCCESS_CODE) {
            playerAccountService.giveCurrency(PlayerCurrencyOperationCommand.builder()
                    .playerUuid(moneyBundleTrade.getPlayerUuid())
                    .amount(moneyBundleTrade.getCurrencyAmount())
                    .currencyId(moneyBundleTrade.getCurrencyId())
                    .build()
            );
        }

        moneyBundleTrade.setStatus(
                paymentStatusCode == PaymentStatusCodes.SUCCESS_CODE
                        ? MoneyBundleTradeStatus.SUCCEEDED
                        : MoneyBundleTradeStatus.FAILED
        );

        moneyBundleTrade.getPayment().setFinishTimestamp(paymentFinishTimestamp);

        moneyBundleTradeRepository.save(moneyBundleTrade);

        log.info("Save money bundle trade {}", moneyBundleTrade.getUuid());
    }
}
