package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunType;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "gun")
@Data
@FieldNameConstants
public class Gun {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gun_gen")
    @SequenceGenerator(name = "seq_gun_gen", sequenceName = "seq_gun", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "enabled")
    private Boolean enabled = true;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Positive
    @Column(name = "weight_grams")
    private Integer weightGrams;

    @Positive
    @Column(name = "rate_of_fire_per_minute")
    private Integer rateOfFirePerMinute;

    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private GunType type;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "ammunition_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "ammunition_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_ammunition"))
    private List<Ammunition> compatibleAmmunitionList;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "ammunition_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "ammunition_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_ammunition"))
    @SQLRestriction("enabled = true")
    private List<Ammunition> enabledCompatibleAmmunitionList;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "attachment_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "attachment_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_attachment"))
    private List<Attachment> compatibleAttachments;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "attachment_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "attachment_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_attachment"))
    @SQLRestriction("enabled = true")
    private List<Attachment> enabledCompatibleAttachments;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;
}
