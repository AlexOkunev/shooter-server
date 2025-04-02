package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Integer>, JpaSpecificationExecutor<Currency> {
    Optional<Currency> findByIdAndEnabled(int id, boolean enabled);
}
