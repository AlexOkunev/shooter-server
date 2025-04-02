package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "grenade")
@Data
@FieldNameConstants
public class Grenade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grenade_gen")
    @SequenceGenerator(name = "seq_grenade_gen", sequenceName = "seq_grenade", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "enabled")
    private Boolean enabled = true;

    @NotBlank
    @Column(name = "name")
    private String name;

    @PositiveOrZero
    @Column(name = "blast_damage_radius_meters")
    private Integer blastDamageRadiusMeters;

    @PositiveOrZero
    @Column(name = "max_blast_damage_hp")
    private Integer maxBlastDamageHP;

    @PositiveOrZero
    @Column(name = "max_blind_time_ms")
    private Integer maxBlindTimeMs;

    @PositiveOrZero
    @Column(name = "max_deaf_time_ms")
    private Integer maxDeafTimeMs;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;
}
