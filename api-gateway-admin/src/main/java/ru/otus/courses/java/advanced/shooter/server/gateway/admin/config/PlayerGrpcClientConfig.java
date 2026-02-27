package ru.otus.courses.java.advanced.shooter.server.gateway.admin.config;

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
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.PlayerGrpcProperties;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.ShooterPlayersServiceAPIGrpc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcRetryUtils.buildServiceConfig;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PlayerGrpcClientConfig {

    private final PlayerGrpcProperties playerGrpcProperties;

    public static final String PLAYER_CHANNEL_NAME = "playerChannel";

    @Bean(PLAYER_CHANNEL_NAME)
    public ManagedChannel managedChannel() {
        NettyChannelBuilder builder = NettyChannelBuilder.forAddress(
                        playerGrpcProperties.getServer().host(),
                        playerGrpcProperties.getServer().port()
                )
                .usePlaintext();

        if (StringUtils.isNotBlank(playerGrpcProperties.getServer().overrideAuthority())) {
            builder.overrideAuthority(playerGrpcProperties.getServer().overrideAuthority());
        }

        if (playerGrpcProperties.getRetry().enabled()) {
            log.info("Enabling retry with props: {}", playerGrpcProperties.getRetry());
            builder.enableRetry();
            builder.defaultServiceConfig(
                    buildServiceConfig(
                            playerGrpcProperties.getRetry(),
                            List.of(
                                    ShooterPlayersServiceAPIGrpc.getGetPlayerMethod(),
                                    ShooterPlayersServiceAPIGrpc.getGetPlayersMethod()
                            )
                    )
            );

            builder.maxRetryAttempts(playerGrpcProperties.getRetry().maxAttempts());
        }

        return builder.build();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ShooterPlayersServiceAPIGrpc.ShooterPlayersServiceAPIBlockingStub playerServiceAPIBlockingStub(
            @Qualifier(PLAYER_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return ShooterPlayersServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(playerGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }
}