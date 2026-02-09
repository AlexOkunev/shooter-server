package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.job.StuckMoneyBundleTradesProcessingJob;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties.StuckMoneyBundleTradeProcessingProperties;

@Configuration
@RequiredArgsConstructor
public class ScheduledTaskConfig implements SchedulingConfigurer {

    private final StuckMoneyBundleTradesProcessingJob stuckMoneyBundleTradesProcessingJob;
    private final StuckMoneyBundleTradeProcessingProperties stuckMoneyBundleTradeProcessingProperties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addFixedRateTask(
                new FixedRateTask(
                        stuckMoneyBundleTradesProcessingJob,
                        stuckMoneyBundleTradeProcessingProperties.getPeriod(),
                        stuckMoneyBundleTradeProcessingProperties.getPeriod()
                )
        );
    }
}
