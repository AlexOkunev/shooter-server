package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.properties.ReferenceDataCachingProperties;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReferenceEquipmentCacheScheduledTaskRegistrar implements SchedulingConfigurer {

    private final ReferenceEquipmentCacheService referenceEquipmentCacheService;

    private final ReferenceDataCachingProperties referenceDataCachingProperties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (referenceDataCachingProperties.isScheduledRefreshCacheEnabled()) {
            taskRegistrar.addFixedRateTask(new FixedRateTask(
                    () -> {
                        log.info("Refresh cache for equipment cache");
                        referenceEquipmentCacheService.reloadAllData();
                        log.info("New cache size is: {}", referenceEquipmentCacheService.count());
                    },
                    Duration.ofSeconds(referenceDataCachingProperties.getRefreshRateSeconds()),
                    Duration.ofMillis(100L)
            ));
        }
    }
}
