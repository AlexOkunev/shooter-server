package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InitialPlayerInventoryFilterParams {
    Boolean enabled;
}
