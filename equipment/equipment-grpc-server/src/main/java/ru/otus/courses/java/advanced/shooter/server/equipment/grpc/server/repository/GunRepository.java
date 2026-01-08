package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GunRepository extends JpaRepository<Gun, Integer>, JpaSpecificationExecutor<Gun> {
    @EntityGraph(attributePaths = {
            Gun.Fields.compatibleAttachments,
            Gun.Fields.compatibleAmmunitionSet
    })
    Optional<Gun> findWithRelatedEntitiesById(int id);

    @EntityGraph(attributePaths = {
            Gun.Fields.enabledCompatibleAttachments,
            Gun.Fields.enabledCompatibleAmmunitionSet
    })
    Optional<Gun> findWithEnabledRelatedEntitiesByIdAndEnabled(int id, boolean enabled);

    @EntityGraph(attributePaths = {Gun.Fields.compatibleAmmunitionSet})
    List<Gun> findWithAmmunitionAllByIdIn(Collection<Integer> ids);

    @EntityGraph(attributePaths = {Gun.Fields.enabledCompatibleAmmunitionSet})
    List<Gun> findWithEnabledAmmunitionAllByIdIn(Collection<Integer> ids);

    @EntityGraph(attributePaths = {Gun.Fields.compatibleAttachments})
    List<Gun> findWithAttachmentsAllByIdIn(Collection<Integer> ids);

    @EntityGraph(attributePaths = {Gun.Fields.enabledCompatibleAttachments})
    List<Gun> findWithEnabledAttachmentsAllByIdIn(Collection<Integer> ids);
}
