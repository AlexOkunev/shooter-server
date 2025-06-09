package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.MoneyBundleTradeStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "money_bundle_trade")
@Data
@FieldNameConstants
public class MoneyBundleTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_money_bundle_trade_gen")
    @SequenceGenerator(name = "seq_money_bundle_trade_gen", sequenceName = "seq_money_bundle_trade", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "player_id")
    private Integer playerId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "money_bundle_id")
    private MoneyBundle moneyBundle;

    @Column(name = "money_bundle_id", insertable = false, updatable = false)
    private Integer moneyBundleId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private ReferenceCurrency currency;

    @Column(name = "currency_id", insertable = false, updatable = false)
    private Integer currencyId;

    @Positive
    @Column(name = "currency_amount")
    private int currencyAmount;

    @Positive
    @Column(name = "rubles_price")
    private int rublesPrice;

    @Version
    @Column(name = "version")
    private int version;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @NotNull
    @Column(name = "status_code")
    @Convert(converter = MoneyBundleTradeStatusConverter.class)
    private MoneyBundleTradeStatus status;

    @NotNull
    @Column(name = "uuid")
    private UUID uuid;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.session, column = @Column(name = "payment_session")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.publicToken, column = @Column(name = "payment_public_token")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.uuid, column = @Column(name = "payment_uuid")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.startTimestamp, column = @Column(name = "payment_start_timestamp")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.finishTimestamp, column = @Column(name = "payment_finish_timestamp"))
    })
    private MoneyBundleTradePayment payment;
}

