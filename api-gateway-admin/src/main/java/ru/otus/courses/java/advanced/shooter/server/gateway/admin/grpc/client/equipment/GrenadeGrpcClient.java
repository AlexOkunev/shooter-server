package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment;


import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;

public interface GrenadeGrpcClient {

    GrenadeInfo getGrenade(GetGrenadeRequest request);

    GrenadeInfo getEnabledGrenade(GetEnabledGrenadeRequest request);

    GrenadeInfoListPage getGrenades(GetGrenadesRequest request);

    GrenadeInfo createGrenade(CreateGrenadeRequest request);

    GrenadeInfo updateGrenade(UpdateGrenadeRequest request);
}
