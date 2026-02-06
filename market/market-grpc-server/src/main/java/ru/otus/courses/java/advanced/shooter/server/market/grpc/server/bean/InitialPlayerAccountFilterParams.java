package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InitialPlayerAccountFilterParams {
    Boolean enabled;
}
