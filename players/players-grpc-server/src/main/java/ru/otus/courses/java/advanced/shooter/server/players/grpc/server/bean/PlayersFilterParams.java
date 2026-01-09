package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.bean;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
@FieldNameConstants
public class PlayersFilterParams {

    String login;
    String email;
    Boolean enabled;

    @Builder.Default
    Set<UUID> playerUuids = Set.of();
}
