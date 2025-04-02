package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

import java.util.Optional;

public interface GunRepository extends JpaRepository<Gun, Integer>, JpaSpecificationExecutor<Gun> {
    Optional<Gun> findByIdAndEnabled(int id, boolean enabled);
}
