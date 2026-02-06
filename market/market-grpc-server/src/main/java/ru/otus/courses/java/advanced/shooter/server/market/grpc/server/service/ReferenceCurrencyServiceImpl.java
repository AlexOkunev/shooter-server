package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceCurrencyCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.ReferenceCurrencyMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ReferenceCurrencyRepository;

@Validated
@Service
@RequiredArgsConstructor
public class ReferenceCurrencyServiceImpl implements ReferenceCurrencyService {

    private final ReferenceCurrencyRepository referenceCurrencyRepository;
    private final ReferenceCurrencyMapper referenceCurrencyMapper;

    @Override
    @Transactional
    public ReferenceCurrency save(@Valid @NotNull SaveReferenceCurrencyCommand command) {
        ReferenceCurrency referenceCurrency = referenceCurrencyMapper.toEntity(command);
        return referenceCurrencyRepository.save(referenceCurrency);
    }
}
