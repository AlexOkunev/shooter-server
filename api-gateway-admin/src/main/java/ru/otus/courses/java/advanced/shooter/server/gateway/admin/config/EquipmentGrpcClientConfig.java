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
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.properties.EquipmentGrpcProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcRetryUtils.buildServiceConfig;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EquipmentGrpcClientConfig {

    private final EquipmentGrpcProperties equipmentGrpcProperties;

    public static final String EQUIPMENT_CHANNEL_NAME = "equipmentChannel";

    @Bean(EQUIPMENT_CHANNEL_NAME)
    public ManagedChannel managedChannel() {
        NettyChannelBuilder builder = NettyChannelBuilder.forAddress(
                        equipmentGrpcProperties.getServer().host(),
                        equipmentGrpcProperties.getServer().port()
                )
                .usePlaintext();

        if (StringUtils.isNotBlank(equipmentGrpcProperties.getServer().overrideAuthority())) {
            builder.overrideAuthority(equipmentGrpcProperties.getServer().overrideAuthority());
        }

        if (equipmentGrpcProperties.getRetry().enabled()) {
            log.info("Enabling retry with props: {}", equipmentGrpcProperties.getRetry());
            builder.enableRetry();
            builder.defaultServiceConfig(
                    buildServiceConfig(
                            equipmentGrpcProperties.getRetry(),
                            List.of(
                                    AmmunitionServiceAPIGrpc.getGetAmmunitionMethod(),
                                    AmmunitionServiceAPIGrpc.getGetEnabledAmmunitionMethod(),
                                    AmmunitionServiceAPIGrpc.getGetAmmunitionListMethod(),
                                    AttachmentServiceAPIGrpc.getGetAttachmentMethod(),
                                    AttachmentServiceAPIGrpc.getGetEnabledAttachmentMethod(),
                                    AttachmentServiceAPIGrpc.getGetAttachmentsMethod(),
                                    GunServiceAPIGrpc.getGetGunMethod(),
                                    GunServiceAPIGrpc.getGetEnabledGunMethod(),
                                    GunServiceAPIGrpc.getGetGunsMethod(),
                                    CurrencyServiceAPIGrpc.getGetCurrencyMethod(),
                                    CurrencyServiceAPIGrpc.getGetEnabledCurrencyMethod(),
                                    CurrencyServiceAPIGrpc.getGetCurrenciesMethod(),
                                    GrenadeServiceAPIGrpc.getGetGrenadeMethod(),
                                    GrenadeServiceAPIGrpc.getGetEnabledGrenadeMethod(),
                                    GrenadeServiceAPIGrpc.getGetGrenadesMethod()
                            )
                    )
            );

            builder.maxRetryAttempts(equipmentGrpcProperties.getRetry().maxAttempts());
        }

        return builder.build();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CurrencyServiceAPIGrpc.CurrencyServiceAPIBlockingStub currencyServiceAPIBlockingStub(
            @Qualifier(EQUIPMENT_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return CurrencyServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(equipmentGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public GrenadeServiceAPIGrpc.GrenadeServiceAPIBlockingStub grenadeServiceAPIBlockingStub(
            @Qualifier(EQUIPMENT_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return GrenadeServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(equipmentGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public AmmunitionServiceAPIGrpc.AmmunitionServiceAPIBlockingStub ammunitionServiceAPIBlockingStub(
            @Qualifier(EQUIPMENT_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return AmmunitionServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(equipmentGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public AttachmentServiceAPIGrpc.AttachmentServiceAPIBlockingStub attachmentServiceAPIBlockingStub(
            @Qualifier(EQUIPMENT_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return AttachmentServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(equipmentGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public GunServiceAPIGrpc.GunServiceAPIBlockingStub gunServiceAPIBlockingStub(
            @Qualifier(EQUIPMENT_CHANNEL_NAME) ManagedChannel managedChannel
    ) {
        return GunServiceAPIGrpc.newBlockingStub(managedChannel)
                .withDeadline(Deadline.after(equipmentGrpcProperties.getDeadlineMs(), TimeUnit.MILLISECONDS));
    }
}