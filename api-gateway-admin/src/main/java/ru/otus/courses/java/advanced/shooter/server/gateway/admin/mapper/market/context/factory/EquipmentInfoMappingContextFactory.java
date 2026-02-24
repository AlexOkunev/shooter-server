package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.EquipmentInfoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.AmmunitionService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.AttachmentService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.GrenadeService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.GunService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.market.EquipmentUtils;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
//@ConditionalOnProperty(value = "caches.enabled", havingValue = "false")  TODO!!! uncomment
public class EquipmentInfoMappingContextFactory {

    private final GrenadeService grenadeService;
    private final GunService gunService;
    private final AmmunitionService ammunitionService;
    private final AttachmentService attachmentService;

    public Mono<EquipmentInfoMappingContext> createForProducts(Mono<ProductInfoListPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage ->
                create(
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GRENADE),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.AMMUNITION),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.ATTACHMENT),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GUN)
                ));
    }

    public Mono<EquipmentInfoMappingContext> createForProduct(Mono<ProductInfo> productInfoMono) {
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

    public Mono<EquipmentInfoMappingContext> createForProductTrades(Mono<ProductTradeInfoListPage> productTradeInfoListPageMono) {
        return productTradeInfoListPageMono.flatMap(itemsPage ->
                create(
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GRENADE),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.AMMUNITION),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.ATTACHMENT),
                        EquipmentUtils.getEquipmentIds(itemsPage, EquipmentType.GUN)
                ));
    }

    public Mono<EquipmentInfoMappingContext> createForProductTrade(Mono<ProductTradeInfo> productTradeInfoMono) {
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

    private Mono<EquipmentInfoMappingContext> create(Collection<Integer> grenadeIds,
                                                     Collection<Integer> ammunitionIds,
                                                     Collection<Integer> attachmentIds,
                                                     Collection<Integer> gunIds) {
        Mono<Map<Integer, GrenadeInfo>> grenadeInfoMono = grenadeService.fetchByIds(grenadeIds)
                .map(grenadeInfoListPage -> grenadeInfoListPage.getDataList()
                        .stream()
                        .collect(Collectors.toMap(GrenadeInfo::getId, Function.identity()))
                );

        Mono<Map<Integer, AmmunitionInfo>> ammunitionInfoMono = ammunitionService.fetchByIds(ammunitionIds)
                .map(ammunitionInfoListPage -> ammunitionInfoListPage.getDataList()
                        .stream()
                        .collect(Collectors.toMap(AmmunitionInfo::getId, Function.identity()))
                );

        Mono<Map<Integer, AttachmentInfo>> attachmentInfoMono = attachmentService.fetchByIds(attachmentIds)
                .map(attachmentInfoListPage -> attachmentInfoListPage.getDataList()
                        .stream()
                        .collect(Collectors.toMap(AttachmentInfo::getId, Function.identity()))
                );

        Mono<Map<Integer, GunInfo>> gunInfoMono = gunService.fetchByIds(gunIds)
                .map(gunInfoListPage -> gunInfoListPage.getDataList()
                        .stream()
                        .collect(Collectors.toMap(GunInfo::getId, Function.identity()))
                );

        return Mono.zip(grenadeInfoMono, ammunitionInfoMono, attachmentInfoMono, gunInfoMono)
                .map(tuple -> EquipmentInfoMappingContext.builder()
                        .grenadesById(tuple.getT1())
                        .ammunitionById(tuple.getT2())
                        .attachmentsById(tuple.getT3())
                        .gunsById(tuple.getT4())
                        .build()
                );
    }
}
