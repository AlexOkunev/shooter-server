package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.equipment;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrenciesFilter;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfoListPage;

import java.util.Collection;

public interface CurrencyService {

    Mono<CurrencyInfo> getOne(int id);

    Mono<CurrencyInfoListPage> search(CurrenciesFilter requestFilter, PaginationRequest paginationRequest);

    Mono<CurrencyInfoListPage> fetchByIds(Collection<Integer> ids);
}
