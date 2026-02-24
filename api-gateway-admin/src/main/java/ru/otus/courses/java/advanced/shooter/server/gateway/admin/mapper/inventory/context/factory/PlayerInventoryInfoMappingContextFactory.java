package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context.PlayerInventoryInfoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.AmmunitionService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.AttachmentService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.GrenadeService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment.GunService;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.inventory.PlayerInventoryUtils;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlayerInventoryInfoMappingContextFactory {

    private final GrenadeService grenadeService;
    private final GunService gunService;
    private final AmmunitionService ammunitionService;
    private final AttachmentService attachmentService;

    public Mono<PlayerInventoryInfoMappingContext> createForInitialItems(Mono<InitialPlayerInventoryItemsPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage ->
                create(
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.GRENADE),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.AMMUNITION),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.ATTACHMENT),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.GUN)
                ));
    }

    public Mono<PlayerInventoryInfoMappingContext> createForItems(Mono<PlayerInventoryItemsPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage ->
                create(
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.GRENADE),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.AMMUNITION),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.ATTACHMENT),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.GUN)
                ));
    }

    public Mono<PlayerInventoryInfoMappingContext> createForLog(Mono<PlayerInventoryLogPage> logPageMono) {
        return logPageMono.flatMap(logPage ->
                create(
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.GRENADE),
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.AMMUNITION),
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.ATTACHMENT),
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.GUN)
                ));
    }

    private Mono<PlayerInventoryInfoMappingContext> create(Collection<Integer> grenadeIds,
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
                .map(tuple -> PlayerInventoryInfoMappingContext.builder()
                        .grenadesById(tuple.getT1())
                        .ammunitionById(tuple.getT2())
                        .attachmentsById(tuple.getT3())
                        .gunsById(tuple.getT4())
                        .build()
                );
    }
}
