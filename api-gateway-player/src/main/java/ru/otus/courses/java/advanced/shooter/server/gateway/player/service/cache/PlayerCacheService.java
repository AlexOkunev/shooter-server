package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.ResettableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.bean.PlayerCacheEntry;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GunMapper;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.ShooterPlayersServiceAPIGrpc;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class PlayerCacheService extends ResettableCacheServiceImplBase<String, PlayerCacheEntry> {

    private final ObjectFactory<ShooterPlayersServiceAPIGrpc.ShooterPlayersServiceAPIBlockingStub> shooterPlayersServiceAPIBlockingStubObjectFactory;

    public PlayerCacheService(
            ObjectFactory<ShooterPlayersServiceAPIGrpc.ShooterPlayersServiceAPIBlockingStub> shooterPlayersServiceAPIBlockingStubObjectFactory, GunMapper gunMapper
    ) {
        super(new SoftReferenceMapCache<>(new ConcurrentHashMap<>()));
        this.shooterPlayersServiceAPIBlockingStubObjectFactory = shooterPlayersServiceAPIBlockingStubObjectFactory;
    }

    @Override
    protected Optional<PlayerCacheEntry> produceDataById(String keycloakId) {
        log.info("Loading player by keycloak ID: {}", keycloakId);

        PlayerInfo playerInfo = shooterPlayersServiceAPIBlockingStubObjectFactory.getObject().getPlayer(
                GetPlayerRequest.newBuilder()
                        .setKeycloakId(keycloakId)
                        .build()
        );

        log.info("Loaded player by keycloak ID: {}", keycloakId);

        return Optional.of(new PlayerCacheEntry(keycloakId, playerInfo));
    }

    @Override
    protected List<PlayerCacheEntry> produceDataByIds(Collection<String> id) {
        throw new UnsupportedOperationException();
    }

    public PlayerInfo getByKeycloakId(String keycloakId) {
        return getById(keycloakId)
                .map(PlayerCacheEntry::playerInfo)
                .orElseThrow(() -> new ObjectNotFoundException(keycloakId));
    }
}
