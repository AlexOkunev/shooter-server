package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunType;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.converter.GunTypeConverter;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "gun")
@Getter
@Setter
@FieldNameConstants
@ToString(onlyExplicitlyIncluded = true)
public class Gun {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gun_gen")
    @SequenceGenerator(name = "seq_gun_gen", sequenceName = "seq_gun", allocationSize = 1)
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

    @Positive
    @Column(name = "weight_grams")
    @ToString.Include
    private Integer weightGrams;

    @Positive
    @Column(name = "rate_of_fire_per_minute")
    @ToString.Include
    private Integer rateOfFirePerMinute;

    @NotNull
    @Column(name = "type")
    @Convert(converter = GunTypeConverter.class)
    @ToString.Include
    private GunType type;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "ammunition_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "ammunition_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_ammunition"))
    private Set<Ammunition> compatibleAmmunitionSet;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "ammunition_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "ammunition_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_ammunition"))
    @SQLRestriction("enabled = true")
    private Set<Ammunition> enabledCompatibleAmmunitionSet;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "attachment_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "attachment_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_attachment"))
    private Set<Attachment> compatibleAttachments;

    @BatchSize(size = 100)
    @ManyToMany
    @JoinTable(name = "attachment_compatible_gun",
            joinColumns = @JoinColumn(name = "gun_id"), inverseJoinColumns = @JoinColumn(name = "attachment_id"),
            foreignKey = @ForeignKey(name = "fk_gun"), inverseForeignKey = @ForeignKey(name = "fk_attachment"))
    @SQLRestriction("enabled = true")
    private Set<Attachment> enabledCompatibleAttachments;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    @ToString.Include
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
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

        Gun that = (Gun) o;

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