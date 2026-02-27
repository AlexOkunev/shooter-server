package ru.otus.courses.java.advanced.shooter.server.gateway.player.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GrpcSchedulers {
    public static final String EQUIPMENT = "grpcEquipmentScheduler";
    public static final String INVENTORY = "grpcInventoryScheduler";
    public static final String MARKET = "grpcMarketScheduler";
    public static final String PLAYER = "grpcPlayerScheduler";
}
