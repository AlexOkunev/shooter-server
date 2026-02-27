package ru.otus.courses.java.advanced.shooter.server.gateway.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.EquipmentGrpcBlockingPoolProperties;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.InventoryGrpcBlockingPoolProperties;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.MarketGrpcBlockingPoolProperties;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.PlayerGrpcBlockingPoolProperties;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;

@Configuration
public class GrpcBlockingPoolsConfig {

    @Bean(name = GrpcSchedulers.EQUIPMENT, destroyMethod = "dispose")
    public Scheduler equipmentScheduler(EquipmentGrpcBlockingPoolProperties properties) {
        return Schedulers.newBoundedElastic(
                properties.getProperties().maxThreads(),
                properties.getProperties().maxQueuedTasks(),
                properties.getProperties().threadNamePrefix()
        );
    }

    @Bean(name = GrpcSchedulers.INVENTORY, destroyMethod = "dispose")
    public Scheduler inventoryScheduler(InventoryGrpcBlockingPoolProperties properties) {
        return Schedulers.newBoundedElastic(
                properties.getProperties().maxThreads(),
                properties.getProperties().maxQueuedTasks(),
                properties.getProperties().threadNamePrefix()
        );
    }

    @Bean(name = GrpcSchedulers.MARKET, destroyMethod = "dispose")
    public Scheduler marketScheduler(MarketGrpcBlockingPoolProperties properties) {
        return Schedulers.newBoundedElastic(
                properties.getProperties().maxThreads(),
                properties.getProperties().maxQueuedTasks(),
                properties.getProperties().threadNamePrefix()
        );
    }

    @Bean(name = GrpcSchedulers.PLAYER, destroyMethod = "dispose")
    public Scheduler playerScheduler(PlayerGrpcBlockingPoolProperties properties) {
        return Schedulers.newBoundedElastic(
                properties.getProperties().maxThreads(),
                properties.getProperties().maxQueuedTasks(),
                properties.getProperties().threadNamePrefix()
        );
    }
}