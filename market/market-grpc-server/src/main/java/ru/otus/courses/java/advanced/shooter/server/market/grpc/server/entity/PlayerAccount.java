package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "player_account")
public class PlayerAccount {
    @Id
    @Column(name = "player_id")
    private Integer playerId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;
}
