package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.EquipmentDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.AmmunitionDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.AttachmentDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.GrenadeDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.GunDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.market.EquipmentUtils;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class EquipmentDtoCachingMappingContextFactory {

    private final GrenadeDtoCacheService grenadeDtoCacheService;
    private final GunDtoCacheService gunDtoCacheService;
    private final AmmunitionDtoCacheService ammunitionDtoCacheService;
    private final AttachmentDtoCacheService attachmentDtoCacheService;

    public Mono<EquipmentDtoMappingContext> createForProducts(Mono<ProductInfoListPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage ->
                create(
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GRENADE),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.AMMUNITION),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.ATTACHMENT),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GUN)
                ));
    }

    public Mono<EquipmentDtoMappingContext> createForProduct(Mono<ProductInfo> productInfoMono) {
        return productInfoMono.flatMap(productInfo ->
                create(
                        productInfo.getEquipment().getEquipmentType() == EquipmentType.GRENADE
                                ? Set.of(productInfo.getEquipment().getEquipmentId()) : Set.of(),
                        productInfo.getEquipment().getEquipmentType() == EquipmentType.AMMUNITION
                                ? Set.of(productInfo.getEquipment().getEquipmentId()) : Set.of(),
                        productInfo.getEquipment().getEquipmentType() == EquipmentType.ATTACHMENT
                                ? Set.of(productInfo.getEquipment().getEquipmentId()) : Set.of(),
                        productInfo.getEquipment().getEquipmentType() == EquipmentType.GUN
                                ? Set.of(productInfo.getEquipment().getEquipmentId()) : Set.of()
                ));
    }

    public Mono<EquipmentDtoMappingContext> createForProductTrades(Mono<ProductTradeInfoListPage> productTradeInfoListPageMono) {
        return productTradeInfoListPageMono.flatMap(itemsPage ->
                create(
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GRENADE),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.AMMUNITION),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.ATTACHMENT),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GUN)
                ));
    }

    public Mono<EquipmentDtoMappingContext> createForProductTrade(Mono<ProductTradeInfo> productTradeInfoMono) {
        return productTradeInfoMono.flatMap(productInfo ->
                create(
                        productInfo.getEquipmentType() == EquipmentType.GRENADE
                                ? Set.of(productInfo.getEquipmentId()) : Set.of(),
                        productInfo.getEquipmentType() == EquipmentType.AMMUNITION
                                ? Set.of(productInfo.getEquipmentId()) : Set.of(),
                        productInfo.getEquipmentType() == EquipmentType.ATTACHMENT
                                ? Set.of(productInfo.getEquipmentId()) : Set.of(),
                        productInfo.getEquipmentType() == EquipmentType.GUN
                                ? Set.of(productInfo.getEquipmentId()) : Set.of()
                ));
    }

    private Mono<EquipmentDtoMappingContext> create(Collection<Integer> grenadeIds,
                                                    Collection<Integer> ammunitionIds,
                                                    Collection<Integer> attachmentIds,
                                                    Collection<Integer> gunIds) {
        Mono<Map<Integer, GrenadeDto>> grenadeInfoMono = Mono.fromCallable(() ->
                grenadeDtoCacheService.getByIdsAsMap(grenadeIds)
        );

        Mono<Map<Integer, AmmunitionDto>> ammunitionInfoMono = Mono.fromCallable(() ->
                ammunitionDtoCacheService.getByIdsAsMap(ammunitionIds)
        );

        Mono<Map<Integer, AttachmentDto>> attachmentInfoMono = Mono.fromCallable(() ->
                attachmentDtoCacheService.getByIdsAsMap(attachmentIds)
        );

        Mono<Map<Integer, GunDto>> gunInfoMono = Mono.fromCallable(() ->
                gunDtoCacheService.getByIdsAsMap(gunIds)
        );

        return Mono.zip(grenadeInfoMono, ammunitionInfoMono, attachmentInfoMono, gunInfoMono)
                .map(tuple -> EquipmentDtoMappingContext.builder()
                        .grenadesById(tuple.getT1())
                        .ammunitionById(tuple.getT2())
                        .attachmentsById(tuple.getT3())
                        .gunsById(tuple.getT4())
                        .build()
                );
    }
}
