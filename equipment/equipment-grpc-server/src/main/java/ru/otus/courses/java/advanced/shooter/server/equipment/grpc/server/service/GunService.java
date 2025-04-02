package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;

public interface GunService {

    GunInfo getGunInfo(int gunId);

    GunInfo getEnabledGunInfo(int gunId);

    GunInfo createGun(CreateGunRequest request);

    GunInfo updateGun(UpdateGunRequest request);

    GunInfoListPage getGuns(GetGunsRequest request);
}
