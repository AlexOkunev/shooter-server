package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer>, JpaSpecificationExecutor<Attachment> {

    @EntityGraph(attributePaths = {Attachment.Fields.enabledCompatibleGuns})
    Optional<Attachment> findWithEnabledGunsByIdAndEnabledIsTrue(int id);

    @EntityGraph(attributePaths = {Attachment.Fields.compatibleGuns})
    Optional<Attachment> findWithGunsById(int id);

    @EntityGraph(attributePaths = {Attachment.Fields.compatibleGuns})
    List<Attachment> findWithGunsAllByIdIn(Collection<Integer> ids);

    @EntityGraph(attributePaths = {Attachment.Fields.enabledCompatibleGuns})
    List<Attachment> findWithEnabledGunsAllByIdIn(Collection<Integer> ids);
}
