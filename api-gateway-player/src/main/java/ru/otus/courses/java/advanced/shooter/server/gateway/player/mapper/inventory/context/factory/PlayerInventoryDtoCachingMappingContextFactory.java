package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.PlayerInventoryDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.AmmunitionDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.AttachmentDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.GrenadeDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache.GunDtoCacheService;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.inventory.PlayerInventoryUtils;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class PlayerInventoryDtoCachingMappingContextFactory {

    private final GrenadeDtoCacheService grenadeDtoCacheService;
    private final GunDtoCacheService gunDtoCacheService;
    private final AmmunitionDtoCacheService ammunitionDtoCacheService;
    private final AttachmentDtoCacheService attachmentDtoCacheService;

    public Mono<PlayerInventoryDtoMappingContext> createForItems(Mono<PlayerInventoryItemsPage> itemsPageMono) {
        return itemsPageMono.flatMap(itemsPage ->
                create(
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.GRENADE),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.AMMUNITION),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.ATTACHMENT),
                        PlayerInventoryUtils.getEquipmentIds(itemsPage, EquipmentType.GUN)
                ));
    }

    public Mono<PlayerInventoryDtoMappingContext> createForLog(Mono<PlayerInventoryLogPage> logPageMono) {
        return logPageMono.flatMap(logPage ->
                create(
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.GRENADE),
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.AMMUNITION),
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.ATTACHMENT),
                        PlayerInventoryUtils.getEquipmentIds(logPage, EquipmentType.GUN)
                ));
    }

    private Mono<PlayerInventoryDtoMappingContext> create(Collection<Integer> grenadeIds,
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
                .map(tuple -> PlayerInventoryDtoMappingContext.builder()
                        .grenadesById(tuple.getT1())
                        .ammunitionById(tuple.getT2())
                        .attachmentsById(tuple.getT3())
                        .gunsById(tuple.getT4())
                        .build()
                );
    }
}
