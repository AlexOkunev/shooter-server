package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.mapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.bean.PlayersFilterParams;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayersRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PlayerMapper {
    public PlayerInfo toResponse(Player player) {
        PlayerInfo.Builder builder = PlayerInfo.newBuilder()
                .setPlayerUuid(player.getPlayerUuid().toString());

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

        builder.setEnabled(player.getEnabled());

        return builder.build();
    }

    public Pageable toPageable(GetPlayersRequest request) {
        return request.hasPaginationRequest()
                ? PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), Sort.by(Sort.Direction.ASC, Player.Fields.createdTimestamp))
                : PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Player.Fields.createdTimestamp));
    }

    public PlayersFilterParams toFilterParams(GetPlayersRequest request) {
        if (!request.hasFilter()) {
            return PlayersFilterParams.builder().build();
        }

        Set<UUID> playerUuids = request.getFilter().getPlayerUuidsList().stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return PlayersFilterParams.builder()
                .email(request.getFilter().getEmail())
                .login(request.getFilter().getLogin())
                .enabled(request.getFilter().getEnabled())
                .playerUuids(playerUuids)
                .build();
    }
}
