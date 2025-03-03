package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;

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
}

//TODO!!! make case insensitive
