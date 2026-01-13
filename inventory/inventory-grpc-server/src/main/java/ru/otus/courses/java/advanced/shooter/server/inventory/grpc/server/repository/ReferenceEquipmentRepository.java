package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipment;

@Repository
public interface ReferenceEquipmentRepository extends JpaRepository<ReferenceEquipment, ReferenceEquipmentId> {
}
