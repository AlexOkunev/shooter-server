package ru.otus.courses.java.advanced.shooter.server.gateway.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.cache.*;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Slf4j
@Configuration
public class ScheduledTaskConfig {

    @Bean
    @ConditionalOnProperty(
            name = {
                    "caches.enabled",
                    "caches.currency.refresh-by-schedule-enabled"
            },
            havingValue = "true"
    )
    public ScheduledFuture<?> refreshCurrencyCacheTask(TaskScheduler taskScheduler,
                                                       CurrencyDtoCacheService currencyDtoCacheService,
                                                       @Value("${caches.currency.refresh-interval-ms:3600000}") long interval) {
        return taskScheduler.schedule(
                () -> {
                    log.info("Refreshing currency cache");
                    currencyDtoCacheService.refresh();
                    log.info("Currency cache refreshed. Current size: {}", currencyDtoCacheService.count());
                },
                new PeriodicTrigger(Duration.ofMillis(interval)));

    }

    @Bean
    @ConditionalOnProperty(
            name = {
                    "caches.enabled",
                    "caches.gun.refresh-by-schedule-enabled"
            },
            havingValue = "true"
    )
    public ScheduledFuture<?> refreshGunCacheTask(TaskScheduler taskScheduler,
                                                  GunDtoCacheService gunDtoCacheService,
                                                  @Value("${caches.gun.refresh-interval-ms:3600000}") long interval) {
        return taskScheduler.schedule(
                () -> {
                    log.info("Refreshing gun cache");
                    gunDtoCacheService.refresh();
                    log.info("Gun cache refreshed. Current size: {}", gunDtoCacheService.count());
                },
                new PeriodicTrigger(Duration.ofMillis(interval)));

    }

    @Bean
    @ConditionalOnProperty(
            name = {
                    "caches.enabled",
                    "caches.grenade.refresh-by-schedule-enabled"
            },
            havingValue = "true"
    )
    public ScheduledFuture<?> refreshGrenadeCacheTask(TaskScheduler taskScheduler,
                                                      GrenadeDtoCacheService grenadeDtoCacheService,
                                                      @Value("${caches.grenade.refresh-interval-ms:3600000}") long interval) {
        return taskScheduler.schedule(
                () -> {
                    log.info("Refreshing grenade cache");
                    grenadeDtoCacheService.refresh();
                    log.info("Grenade cache refreshed. Current size: {}", grenadeDtoCacheService.count());
                },
                new PeriodicTrigger(Duration.ofMillis(interval)));

    }

    @Bean
    @ConditionalOnProperty(
            name = {
                    "caches.enabled",
                    "caches.ammunition.refresh-by-schedule-enabled"
            },
            havingValue = "true"
    )
    public ScheduledFuture<?> refreshAmmunitionCacheTask(TaskScheduler taskScheduler,
                                                         AmmunitionDtoCacheService ammunitionDtoCacheService,
                                                         @Value("${caches.ammunition.refresh-interval-ms:3600000}") long interval) {
        return taskScheduler.schedule(
                () -> {
                    log.info("Refreshing ammunition cache");
                    ammunitionDtoCacheService.refresh();
                    log.info("Ammunition cache refreshed. Current size: {}", ammunitionDtoCacheService.count());
                },
                new PeriodicTrigger(Duration.ofMillis(interval)));

    }

    @Bean
    @ConditionalOnProperty(
            name = {
                    "caches.enabled",
                    "caches.attachment.refresh-by-schedule-enabled"
            },
            havingValue = "true"
    )
    public ScheduledFuture<?> refreshAttachmentCacheTask(TaskScheduler taskScheduler,
                                                         AttachmentDtoCacheService attachmentDtoCacheService,
                                                         @Value("${caches.attachment.refresh-interval-ms:3600000}") long interval) {
        return taskScheduler.schedule(
                () -> {
                    log.info("Refreshing attachment cache");
                    attachmentDtoCacheService.refresh();
                    log.info("Attachment cache refreshed. Current size: {}", attachmentDtoCacheService.count());
                },
                new PeriodicTrigger(Duration.ofMillis(interval)));

    }

    @Bean
    public TaskScheduler taskScheduler(@Value("${scheduling.pool-size:1}") int poolSize) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("my-sched-");
        taskScheduler.setPoolSize(poolSize);

        taskScheduler.initialize();

        ((ScheduledThreadPoolExecutor) taskScheduler.getScheduledExecutor())
                .setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        ((ScheduledThreadPoolExecutor) taskScheduler.getScheduledExecutor())
                .setExecuteExistingDelayedTasksAfterShutdownPolicy(false);

        return taskScheduler;
    }
}
