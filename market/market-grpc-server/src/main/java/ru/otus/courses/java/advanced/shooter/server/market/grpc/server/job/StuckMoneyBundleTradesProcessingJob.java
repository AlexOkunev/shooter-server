package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties.StuckMoneyBundleTradeProcessingProperties;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleTradeRepository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Slf4j
@Component
@ConditionalOnProperty(value = "stuck-money-bundle-trade-processing.enabled", havingValue = "true")
@RequiredArgsConstructor
public class StuckMoneyBundleTradesProcessingJob implements Runnable {

    private final MoneyBundleTradeRepository moneyBundleTradeRepository;
    private final StuckMoneyBundleTradeProcessingProperties stuckMoneyBundleTradeProcessingProperties;

    @Override
    public void run() {
        ZonedDateTime stuckInPaymentCreationWaitTime = ZonedDateTime.now(ZoneOffset.UTC).minus(
                stuckMoneyBundleTradeProcessingProperties.getPaymentCreationWaitProcessing().getMaxStuckPeriod()
        );

        ZonedDateTime stuckInPaymentPendingTime = ZonedDateTime.now(ZoneOffset.UTC).minus(
                stuckMoneyBundleTradeProcessingProperties.getPaymentPendingProcessing().getMaxStuckPeriod()
        );

        if (stuckMoneyBundleTradeProcessingProperties.getPaymentCreationWaitProcessing().isEnabled()) {
            log.info("Move PAYMENT_CREATION_WAIT money bundle trades updated before {} to status CREATED", stuckInPaymentCreationWaitTime);

            int count = moneyBundleTradeRepository.updateStatusForTrades(
                    MoneyBundleTradeStatus.PAYMENT_CREATION_WAIT,
                    MoneyBundleTradeStatus.CREATED,
                    stuckInPaymentCreationWaitTime,
                    ZonedDateTime.now(ZoneOffset.UTC)
            );

            log.info("Moved {} money bundle trades to CREATED", count);
        }

        if (stuckMoneyBundleTradeProcessingProperties.getPaymentPendingProcessing().isEnabled()) {
            log.info("Move PAYMENT_PENDING money bundle trades updated before {} to status TIMEOUT", stuckInPaymentCreationWaitTime);

            int count = moneyBundleTradeRepository.updateStatusForTrades(
                    MoneyBundleTradeStatus.PAYMENT_PENDING,
                    MoneyBundleTradeStatus.TIMEOUT,
                    stuckInPaymentPendingTime,
                    ZonedDateTime.now(ZoneOffset.UTC)
            );

            log.info("Moved {} money bundle trades to TIMEOUT", count);
        }
    }
}
