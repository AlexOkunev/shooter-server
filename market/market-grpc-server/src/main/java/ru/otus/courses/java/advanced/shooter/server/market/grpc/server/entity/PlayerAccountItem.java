package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@Table(name = "player_account_item")
@FieldNameConstants
public class PlayerAccountItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_player_account_item_gen")
    @SequenceGenerator(name = "seq_player_account_item_gen", sequenceName = "seq_player_account_item", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "player_id")
    private Integer playerId;

    @NotNull
    @Min(0)
    @Column(name = "currency_amount")
    private Integer currencyAmount;

    @Column(name = "currency_id", insertable = false, updatable = false)
    private Integer currencyId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private ReferenceCurrency currency;

    @Version
    @Column(name = "version")
    private int version;
}
