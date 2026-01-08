package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentType;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.converter.AttachmentTypeConverter;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@FieldNameConstants
@ToString(onlyExplicitlyIncluded = true)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_attachment_gen")
    @SequenceGenerator(name = "seq_attachment_gen", sequenceName = "seq_attachment", allocationSize = 1)
    @Column(name = "id")
    @ToString.Include
    private Integer id;

    @Column(name = "enabled")
    @ToString.Include
    private Boolean enabled = true;

    @NotBlank
    @Column(name = "name")
    @ToString.Include
    private String name;

    @NotNull
    @Column(name = "type")
    @Convert(converter = AttachmentTypeConverter.class)
    @ToString.Include
    private AttachmentType type;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = AttachmentEffect.Fields.soundLoudnessRate, column = @Column(name = "effect_sound_loudness_rate")),
            @AttributeOverride(name = AttachmentEffect.Fields.maxZoomRate, column = @Column(name = "effect_max_zoom_rate")),
            @AttributeOverride(name = AttachmentEffect.Fields.blowBackRate, column = @Column(name = "effect_blow_back_rate")),
            @AttributeOverride(name = AttachmentEffect.Fields.laserMaxDistanceMeters, column = @Column(name = "effect_laser_max_distance_meters")),
            @AttributeOverride(name = AttachmentEffect.Fields.bulletSpeedRate, column = @Column(name = "effect_bullet_speed_rate")),
    })
    @ToString.Include
    private AttachmentEffect effect;

    @ManyToMany
    @BatchSize(size = 100)
    @JoinTable(name = "attachment_compatible_gun",
            joinColumns = @JoinColumn(name = "attachment_id"), inverseJoinColumns = @JoinColumn(name = "gun_id"),
            foreignKey = @ForeignKey(name = "fk_attachment"), inverseForeignKey = @ForeignKey(name = "fk_gun"))
    private Set<Gun> compatibleGuns;

    @ManyToMany
    @BatchSize(size = 100)
    @JoinTable(name = "attachment_compatible_gun",
            joinColumns = @JoinColumn(name = "attachment_id"), inverseJoinColumns = @JoinColumn(name = "gun_id"),
            foreignKey = @ForeignKey(name = "fk_attachment"), inverseForeignKey = @ForeignKey(name = "fk_gun"))
    @SQLRestriction("enabled = true")
    private Set<Gun> enabledCompatibleGuns;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_timestamp")
    @ToString.Include
    private ZonedDateTime createdTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_timestamp")
    @ToString.Include
    private ZonedDateTime updatedTimestamp;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Attachment that = (Attachment) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}

//Annotation @Data is not used and equals and hashCode are overridden because if issues with sets of related entities
//See https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/#using-a-generated-primary-key
//https://vladmihalcea.com/hibernate-facts-equals-and-hashcode
//https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/