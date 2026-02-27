package ru.otus.courses.java.advanced.shooter.server.gateway.player.config;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.properties.InventoryGrpcProperties;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogServiceAPIGrpc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcRetryUtils.buildServiceConfig;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InventoryGrpcClientConfig {

    private final InventoryGrpcProperties inventoryGrpcProperties;

    public static final String INVENTORY_CHANNEL_NAME = "inventoryChannel";

    @Bean(INVENTORY_CHANNEL_NAME)
    public ManagedChannel managedChannel() {
        NettyChannelBuilder builder = NettyChannelBuilder.forAddress(
                        inventoryGrpcProperties.getServer().host(),
                        inventoryGrpcProperties.getServer().port()
                )
                .usePlaintext();

        if (StringUtils.isNotBlank(inventoryGrpcProperties.getServer().overrideAuthority())) {
            builder.overrideAuthority(inventoryGrpcProperties.getServer().overrideAuthority());
        }

        if (inventoryGrpcProperties.getRetry().enabled()) {
            log.info("Enabling retry with props: {}", inventoryGrpcProperties.getRetry());
            builder.enableRetry();
            builder.defaultServiceConfig(
                    buildServiceConfig(
                            inventoryGrpcProperties.getRetry(),
                            List.of(
                                    PlayerInventoryServiceAPIGrpc.getGetPlayerInventoryMethod(),
                                    PlayerInventoryLogServiceAPIGrpc.getGetPlayerInventoryLogMethod(),
                                    InitialPlayerInventoryServiceAPIGrpc.getGetInitialPlayerInventoryMethod()
                            )
                    )
            );

            builder.maxRetryAttempts(inventoryGrpcProperties.getRetry().maxAttempts());
        }


        return builder.build();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public PlayerInventoryServiceAPIGrpc.PlayerInventoryServiceAPIBlockingStub inventoryServiceAPIBlockingStub(
            @Qualifier(INVENTORY_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return PlayerInventoryServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(inventoryGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public PlayerInventoryLogServiceAPIGrpc.PlayerInventoryLogServiceAPIBlockingStub inventoryLogServiceAPIBlockingStub(
            @Qualifier(INVENTORY_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return PlayerInventoryLogServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(inventoryGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }
}