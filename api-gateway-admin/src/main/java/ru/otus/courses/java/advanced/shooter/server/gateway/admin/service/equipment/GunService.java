package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionWritableData;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunWritableData;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunsFilter;

import java.util.Collection;

public interface GunService {

    Mono<GunInfo> getOne(int id);

    Mono<GunInfoListPage> search(GunsFilter requestFilter, PaginationRequest paginationRequest);

    Mono<GunInfoListPage> fetchByIds(Collection<Integer> ids);

    Mono<GunInfo> create(GunWritableData data);

    Mono<GunInfo> update(int id, GunWritableData data);
}
