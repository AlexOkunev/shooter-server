package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;

public interface GrenadeService {

    GrenadeInfo getGrenadeInfo(int grenadeId);

    GrenadeInfo getEnabledGrenadeInfo(int grenadeId);

    GrenadeInfo createGrenade(CreateGrenadeRequest request);

    GrenadeInfo updateGrenade(UpdateGrenadeRequest request);

    GrenadeInfoListPage getGrenades(GetGrenadesRequest request);
}
