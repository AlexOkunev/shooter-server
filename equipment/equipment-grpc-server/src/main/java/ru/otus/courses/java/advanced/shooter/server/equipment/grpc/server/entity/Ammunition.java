package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "ammunition")
@Getter
@Setter
@FieldNameConstants
@ToString(onlyExplicitlyIncluded = true)
public class Ammunition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ammunition_gen")
    @SequenceGenerator(name = "seq_ammunition_gen", sequenceName = "seq_ammunition", allocationSize = 1)
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
    @Column(name = "speed")
    @ToString.Include
    private Integer speed;

    @Positive
    @Column(name = "damage_mean_value")
    @ToString.Include
    private Integer damageMeanValue;

    @Positive
    @Column(name = "damage_variance")
    @ToString.Include
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

        Ammunition that = (Ammunition) o;

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