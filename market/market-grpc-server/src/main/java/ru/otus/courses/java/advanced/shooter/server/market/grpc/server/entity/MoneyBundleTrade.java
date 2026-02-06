package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.MoneyBundleTradeStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "money_bundle_trade")
@IdClass(TradeId.class)
@Getter
@Setter
@FieldNameConstants
@ToString(exclude = {"currency", "moneyBundle"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MoneyBundleTrade {

    @Id
    @Column(name = "player_uuid")
    @EqualsAndHashCode.Include
    private UUID playerUuid;

    @Id
    @Column(name = "uuid")
    @EqualsAndHashCode.Include
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "money_bundle_id", insertable = false, updatable = false)
    private MoneyBundle moneyBundle;

    @Column(name = "money_bundle_id")
    private Integer moneyBundleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private ReferenceCurrency currency;

    @Column(name = "currency_id")
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
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @NotNull
    @Column(name = "status_code")
    @Convert(converter = MoneyBundleTradeStatusConverter.class)
    private MoneyBundleTradeStatus status;

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.paymentSession, column = @Column(name = "payment_session")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.publicToken, column = @Column(name = "payment_public_token")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.uuid, column = @Column(name = "payment_uuid")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.startTimestamp, column = @Column(name = "payment_start_timestamp")),
            @AttributeOverride(name = MoneyBundleTradePayment.Fields.finishTimestamp, column = @Column(name = "payment_finish_timestamp"))
    })
    private MoneyBundleTradePayment payment;
}

