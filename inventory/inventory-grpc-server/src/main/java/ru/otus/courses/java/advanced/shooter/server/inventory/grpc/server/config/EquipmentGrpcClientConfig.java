package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.properties.EquipmentGrpcServerProperties;

@Configuration
public class EquipmentGrpcClientConfig {

    @Bean("equipmentServiceChannel")
    public ManagedChannel getEquipmentServiceChannel(EquipmentGrpcServerProperties equipmentGrpcServerProperties) {
        return ManagedChannelBuilder.forAddress(equipmentGrpcServerProperties.getHost(), equipmentGrpcServerProperties.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    public GrenadeServiceAPIGrpc.GrenadeServiceAPIBlockingStub getGrenadeServiceBlockingStub(ManagedChannel equipmentServiceChannel) {
        return GrenadeServiceAPIGrpc.newBlockingStub(equipmentServiceChannel);
    }

    @Bean
    public GunServiceAPIGrpc.GunServiceAPIBlockingStub getGunServiceBlockingStub(ManagedChannel equipmentServiceChannel) {
        return GunServiceAPIGrpc.newBlockingStub(equipmentServiceChannel);
    }

    @Bean
    public AmmunitionServiceAPIGrpc.AmmunitionServiceAPIBlockingStub getAmmunitionServiceBlockingStub(ManagedChannel equipmentServiceChannel) {
        return AmmunitionServiceAPIGrpc.newBlockingStub(equipmentServiceChannel);
    }

    @Bean
    public AttachmentServiceAPIGrpc.AttachmentServiceAPIBlockingStub getAttachmentServiceBlockingStub(ManagedChannel equipmentServiceChannel) {
        return AttachmentServiceAPIGrpc.newBlockingStub(equipmentServiceChannel);
    }
}
