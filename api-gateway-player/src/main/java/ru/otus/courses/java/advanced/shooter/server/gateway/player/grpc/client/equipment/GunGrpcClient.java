package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;


import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;

public interface GunGrpcClient {

    GunInfo getGun(GetGunRequest request);

    GunInfo getEnabledGun(GetEnabledGunRequest request);

    GunInfoListPage getGuns(GetGunsRequest request);

    GunInfo createGun(CreateGunRequest request);

    GunInfo updateGun(UpdateGunRequest request);
}
