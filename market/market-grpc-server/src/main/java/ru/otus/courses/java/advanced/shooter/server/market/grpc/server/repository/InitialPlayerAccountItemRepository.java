package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;

@Repository
public interface InitialPlayerAccountItemRepository extends JpaRepository<InitialPlayerAccountItem, Integer>, JpaSpecificationExecutor<InitialPlayerAccountItem> {
}
