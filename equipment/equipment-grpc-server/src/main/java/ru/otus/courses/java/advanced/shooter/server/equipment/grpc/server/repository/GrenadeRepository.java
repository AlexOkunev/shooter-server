package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;

import java.util.Optional;

public interface GrenadeRepository extends JpaRepository<Grenade, Integer>, JpaSpecificationExecutor<Grenade> {
    Optional<Grenade> findByIdAndEnabled(int id, boolean enabled);
}
