package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.InitialPlayerAccountFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.UpdateInitialPlayerAccountCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;

public interface InitialPlayerAccountService {
    Page<InitialPlayerAccountItem> getInitialPlayerAccountItems(
            @NotNull InitialPlayerAccountFilterParams filterParams,
            @NotNull Pageable pageable
    );

    void saveInitialPlayerAccount(@Valid @NotNull UpdateInitialPlayerAccountCommand command);
}
