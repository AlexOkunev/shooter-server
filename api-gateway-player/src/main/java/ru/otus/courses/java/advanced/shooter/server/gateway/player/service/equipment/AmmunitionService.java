package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.equipment;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionFilter;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfoListPage;

import java.util.Collection;

public interface AmmunitionService {

    Mono<AmmunitionInfo> getOne(int id);

    Mono<AmmunitionInfoListPage> search(AmmunitionFilter requestFilter, PaginationRequest paginationRequest);

    Mono<AmmunitionInfoListPage> fetchByIds(Collection<Integer> ids);
}
