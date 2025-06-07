package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;

import java.util.Optional;

public interface MoneyBundleRepository extends JpaRepository<MoneyBundle, Integer>, JpaSpecificationExecutor<MoneyBundle> {
    Optional<MoneyBundle> findByIdAndEnabledIsTrue(int id);
}
