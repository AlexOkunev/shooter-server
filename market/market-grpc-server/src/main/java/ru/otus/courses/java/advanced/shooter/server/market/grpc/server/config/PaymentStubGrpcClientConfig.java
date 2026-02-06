package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.config;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties.PaymentStubGrpcProperties;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.PaymentServiceAPIGrpc;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class PaymentStubGrpcClientConfig {
    private final PaymentStubGrpcProperties paymentStubGrpcProperties;

    public static final String PAYMENT_STUB_CHANNEL_NAME = "paymentStubChannel";

    @Bean(PAYMENT_STUB_CHANNEL_NAME)
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress(paymentStubGrpcProperties.getHost(), paymentStubGrpcProperties.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public PaymentServiceAPIGrpc.PaymentServiceAPIBlockingStub paymentServiceAPIBlockingStub(
            @Qualifier(PAYMENT_STUB_CHANNEL_NAME) ManagedChannel managedChannel) {
        return PaymentServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(paymentStubGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }
}

//TODO use retry 3 attempts, with bucket. add circuit breaker