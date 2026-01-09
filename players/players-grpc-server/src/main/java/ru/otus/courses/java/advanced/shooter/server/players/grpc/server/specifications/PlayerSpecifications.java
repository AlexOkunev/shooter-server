package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class PlayerSpecifications {
    public static Specification<Player> byLoginStartsWith(String login) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Player.Fields.login)), login.toLowerCase() + "%");
    }

    public static Specification<Player> byEmailStartsWith(String email) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Player.Fields.email)), email.toLowerCase() + "%");
    }

    public static Specification<Player> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(Player.Fields.enabled), enabled);
    }

    public static Specification<Player> byPlayerIds(Set<UUID> playerIds) {
        return (root, query, builder) ->
                builder.in(root.get(Player.Fields.playerUuid)).value(playerIds);
    }
}