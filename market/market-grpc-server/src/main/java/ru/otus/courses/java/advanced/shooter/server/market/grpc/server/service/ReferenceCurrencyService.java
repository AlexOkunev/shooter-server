package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceCurrencyCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;

public interface ReferenceCurrencyService {
    ReferenceCurrency save(@Valid @NotNull SaveReferenceCurrencyCommand command);
}
