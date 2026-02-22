package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadesFilter;

import java.util.Collection;

public interface GrenadeService {

    Mono<GrenadeInfo> getOne(int id);

    Mono<GrenadeInfoListPage> search(GrenadesFilter requestFilter, PaginationRequest paginationRequest);

    Mono<GrenadeInfoListPage> fetchByIds(Collection<Integer> ids);
}
