package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;

import java.util.Optional;

@Repository
public interface AmmunitionRepository extends JpaRepository<Ammunition, Integer>, JpaSpecificationExecutor<Ammunition> {

    Optional<Ammunition> findByIdAndEnabledIsTrue(int id);
}
