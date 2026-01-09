package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "player")
@FieldNameConstants
public class Player {
    @Id
    @Column(name = "keycloak_id")
    private String keycloakId;

    @Column(name = "player_uuid")
    private UUID playerUuid;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "realm_id")
    private String realmId;

    @Column(name = "login")
    private String login;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;
}
