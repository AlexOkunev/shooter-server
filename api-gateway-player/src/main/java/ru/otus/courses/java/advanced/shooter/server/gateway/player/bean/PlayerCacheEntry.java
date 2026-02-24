package ru.otus.courses.java.advanced.shooter.server.gateway.player.bean;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;

public record PlayerCacheEntry(String keycloakId, PlayerInfo playerInfo) implements CacheableData<String> {

    @Override
    public String getId() {
        return keycloakId;
    }

    @Override
    public boolean isEnabled() {
        return playerInfo.getEnabled();
    }
}
