package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;

public interface AmmunitionService {

    AmmunitionInfo getAmmunitionInfo(int ammunitionId);

    AmmunitionInfo getEnabledAmmunitionInfo(int ammunitionId);

    AmmunitionInfo createAmmunition(CreateAmmunitionRequest request);

    AmmunitionInfo updateAmmunition(UpdateAmmunitionRequest request);

    AmmunitionInfoListPage getAmmunitionList(GetAmmunitionListRequest request);
}