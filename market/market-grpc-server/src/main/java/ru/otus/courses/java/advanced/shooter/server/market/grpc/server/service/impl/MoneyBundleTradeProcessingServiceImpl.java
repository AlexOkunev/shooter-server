package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTradePayment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProcessedExternalMessage;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleTradeRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProcessedExternalMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleTradeProcessingService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerCurrencyOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.common.PaymentStatusCodes;
import ru.otus.courses.java.advanced.shooter.server.payment.outbox.ProcessedPaymentMessage;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoneyBundleTradeProcessingServiceImpl implements MoneyBundleTradeProcessingService {

    private final MoneyBundleTradeRepository moneyBundleTradeRepository;

    private final ProcessedExternalMessageRepository processedExternalMessageRepository;

    private final PlayerAccountService playerAccountService;

    private final Random random = new Random();

    @Override
    @Transactional
    public void processMessage(ProcessedPaymentMessage message) {
        log.info("Process message {}. Trade uuid {}", message.getMessageUuid(), message.getTradeUuid());

        Integer a = null;

        if (true) {
            log.info("Number is {}", a + 1);
        } else {
            log.info("No NPE");
        }

        if (!processedExternalMessageRepository.existsByMessageUUID(UUID.fromString(message.getMessageUuid()))) {
            ProcessedExternalMessage processedExternalMessage = new ProcessedExternalMessage();
            processedExternalMessage.setMessageCreatedTimestamp(ZonedDateTime.ofInstant(message.getCreatedTimestamp(), ZoneOffset.UTC));
            processedExternalMessage.setMessageUUID(UUID.fromString(message.getMessageUuid()));
            processedExternalMessage = processedExternalMessageRepository.save(processedExternalMessage);

            log.info("Save processed external message: {}", processedExternalMessage.getMessageUUID());

            completeMoneyBundleTrade(
                    UUID.fromString(message.getTradeUuid()),
                    ZonedDateTime.ofInstant(message.getProcessingFinishedTimestamp(), ZoneOffset.UTC),
                    message.getStatus());
        } else {
            log.error("External message with uuid {} has already been processed", message.getMessageUuid());
        }
    }

    private void completeMoneyBundleTrade(UUID tradeUuid, ZonedDateTime paymentFinishTimestamp, int paymentStatusCode) {
        Optional<MoneyBundleTrade> moneyBundleTradeOptional = moneyBundleTradeRepository.findByUuid(tradeUuid);

        if (moneyBundleTradeOptional.isEmpty()) {
            log.error("Money bundle trade with uuid {} not found", tradeUuid);
            return;
        }

        MoneyBundleTrade moneyBundleTrade = moneyBundleTradeOptional.get();

        if (moneyBundleTrade.getStatus().getCode() != MoneyBundleTradeStatus.PAYMENT_PENDING.getCode()) {
            log.error("Money bundle trade with uuid {} has incorrect status {}. Must be {}",
                    tradeUuid, moneyBundleTrade.getStatus(), MoneyBundleTradeStatus.PAYMENT_PENDING);
            return;
        }

        if (paymentStatusCode != PaymentStatusCodes.SUCCESS_CODE && paymentStatusCode != PaymentStatusCodes.FAILED_CODE) {
            log.error("Message contains incorrect payment status code {}", paymentStatusCode);
            return;
        }

        if (paymentStatusCode == PaymentStatusCodes.SUCCESS_CODE) {
            playerAccountService.giveCurrency(PlayerCurrencyOperationRequest.newBuilder()
                    .setPlayerId(moneyBundleTrade.getPlayerId())
                    .setAmount(moneyBundleTrade.getCurrencyAmount())
                    .setCurrencyId(moneyBundleTrade.getCurrencyId())
                    .build());

            moneyBundleTrade.setStatus(MoneyBundleTradeStatus.SUCCEEDED);
        } else {
            moneyBundleTrade.setStatus(MoneyBundleTradeStatus.FAILED);
        }

        Optional.ofNullable(moneyBundleTrade.getPayment())
                .ifPresentOrElse(
                        payment -> payment.setFinishTimestamp(paymentFinishTimestamp),
                        () -> {
                            MoneyBundleTradePayment newPayment = new MoneyBundleTradePayment();
                            newPayment.setFinishTimestamp(paymentFinishTimestamp);
                            moneyBundleTrade.setPayment(newPayment);
                        });

        moneyBundleTradeRepository.save(moneyBundleTrade);

        log.info("Save money bundle trade {} ({})", moneyBundleTrade.getId(), moneyBundleTrade.getUuid());
    }
}
