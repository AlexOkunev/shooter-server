package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AmmunitionRepository extends JpaRepository<Ammunition, Integer>, JpaSpecificationExecutor<Ammunition> {

    @EntityGraph(attributePaths = {Ammunition.Fields.enabledCompatibleGuns})
    Optional<Ammunition> findWithEnabledBunsByIdAndEnabledIsTrue(int id);

    @EntityGraph(attributePaths = {Ammunition.Fields.compatibleGuns})
    Optional<Ammunition> findWithGunsById(int id);

    @EntityGraph(attributePaths = {Ammunition.Fields.compatibleGuns})
    List<Ammunition> findWithGunsAllByIdIn(Collection<Integer> ids);

    @EntityGraph(attributePaths = {Ammunition.Fields.enabledCompatibleGuns})
    List<Ammunition> findWithEnabledGunsAllByIdIn(Collection<Integer> ids);
}
