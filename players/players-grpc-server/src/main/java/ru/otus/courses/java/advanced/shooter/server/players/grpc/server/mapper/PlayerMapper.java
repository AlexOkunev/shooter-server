package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.mapper;

import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;

@Component
public class PlayerMapper {
    public PlayerInfo toResponse(Player player) {
        PlayerInfo.Builder builder = PlayerInfo.newBuilder()
                .setPlayerId(player.getPlayerId());

        if (player.getFirstName() != null) {
            builder.setFirstName(player.getFirstName());
        }

        if (player.getLastName() != null) {
            builder.setLastName(player.getLastName());
        }

        if (player.getEmail() != null) {
            builder.setEmail(player.getEmail());
        }

        if (player.getLogin() != null) {
            builder.setLogin(player.getLogin());
        }

        if (player.getCreatedTimestamp() != null) {
            builder.setCreatedTimestamp(player.getCreatedTimestamp().toInstant().toEpochMilli());
        }

        return builder.build();
    }
}
