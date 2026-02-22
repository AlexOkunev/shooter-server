package ru.otus.courses.java.advanced.shooter.server.gateway.player.config;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.properties.MarketGrpcProperties;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeServiceAPIGrpc;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class MarketGrpcClientConfig {

    private final MarketGrpcProperties marketGrpcProperties;

    public static final String MARKET_CHANNEL_NAME = "marketChannel";

    @Bean(MARKET_CHANNEL_NAME)
    public ManagedChannel managedChannel() {
        NettyChannelBuilder builder = NettyChannelBuilder.forAddress(
                        marketGrpcProperties.getServer().host(),
                        marketGrpcProperties.getServer().port()
                )
                .usePlaintext();

        if (StringUtils.isNotBlank(marketGrpcProperties.getServer().overrideAuthority())) {
            builder.overrideAuthority(marketGrpcProperties.getServer().overrideAuthority());
        }

        return builder.build();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public PlayerAccountServiceAPIGrpc.PlayerAccountServiceAPIBlockingStub marketAccountServiceAPIBlockingStub(
            @Qualifier(MARKET_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return PlayerAccountServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(marketGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public PlayerAccountLogServiceAPIGrpc.PlayerAccountLogServiceAPIBlockingStub marketAccountLogServiceAPIBlockingStub(
            @Qualifier(MARKET_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return PlayerAccountLogServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(marketGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public MoneyBundleServiceAPIGrpc.MoneyBundleServiceAPIBlockingStub moneyBundleServiceAPIBlockingStub(
            @Qualifier(MARKET_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return MoneyBundleServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(marketGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProductServiceAPIGrpc.ProductServiceAPIBlockingStub productServiceAPIBlockingStub(
            @Qualifier(MARKET_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return ProductServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(marketGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public MoneyBundleTradeServiceAPIGrpc.MoneyBundleTradeServiceAPIBlockingStub moneyBundleTradeServiceAPIBlockingStub(
            @Qualifier(MARKET_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return MoneyBundleTradeServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(marketGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProductTradeServiceAPIGrpc.ProductTradeServiceAPIBlockingStub productTradeServiceAPIBlockingStub(
            @Qualifier(MARKET_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return ProductTradeServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(marketGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }
}

//TODO use retry 3 attempts, with bucket. add circuit breaker