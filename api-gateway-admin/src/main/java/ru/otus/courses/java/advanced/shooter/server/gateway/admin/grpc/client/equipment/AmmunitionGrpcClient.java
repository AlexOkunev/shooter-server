package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment;


import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;

public interface AmmunitionGrpcClient {

    AmmunitionInfo getAmmunition(GetAmmunitionRequest request);

    AmmunitionInfo getEnabledAmmunition(GetEnabledAmmunitionRequest request);

    AmmunitionInfoListPage getAmmunitionList(GetAmmunitionListRequest request);

    AmmunitionInfo createAmmunition(CreateAmmunitionRequest request);

    AmmunitionInfo updateAmmunition(UpdateAmmunitionRequest request);
}
