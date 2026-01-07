package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "ammunition")
@Data
@FieldNameConstants
public class Ammunition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ammunition_gen")
    @SequenceGenerator(name = "seq_ammunition_gen", sequenceName = "seq_ammunition", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "enabled")
    private Boolean enabled = true;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Positive
    @Column(name = "speed")
    private Integer speed;

    @Positive
    @Column(name = "damage_mean_value")
    private Integer damageMeanValue;

    @Positive
    @Column(name = "damage_variance")
    private Integer damageVariance;

    @ManyToMany
    @BatchSize(size = 100)
    @JoinTable(name = "ammunition_compatible_gun",
            joinColumns = @JoinColumn(name = "ammunition_id"), inverseJoinColumns = @JoinColumn(name = "gun_id"),
            foreignKey = @ForeignKey(name = "fk_ammunition"), inverseForeignKey = @ForeignKey(name = "fk_gun"))
    private Set<Gun> compatibleGuns;

    @ManyToMany
    @BatchSize(size = 100)
    @JoinTable(name = "ammunition_compatible_gun",
            joinColumns = @JoinColumn(name = "ammunition_id"), inverseJoinColumns = @JoinColumn(name = "gun_id"),
            foreignKey = @ForeignKey(name = "fk_ammunition"), inverseForeignKey = @ForeignKey(name = "fk_gun"))
    @SQLRestriction("enabled = true")
    private Set<Gun> enabledCompatibleGuns;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;
}
