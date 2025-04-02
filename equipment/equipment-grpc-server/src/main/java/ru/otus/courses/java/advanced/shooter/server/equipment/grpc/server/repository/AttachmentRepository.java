package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer>, JpaSpecificationExecutor<Attachment> {

    Optional<Attachment> findByIdAndEnabledIsTrue(int id);
}
