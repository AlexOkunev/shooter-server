package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.kafka.types.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.kafka.types.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.kafka.types.Grenade;
import ru.otus.courses.java.advanced.shooter.server.equipment.kafka.types.Gun;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceEquipmentCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.config.KafkaListenerConfig;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ReferenceEquipmentService;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = "#{'${equipment-kafka.topics}'.split(',')}",
        groupId = "${equipment-kafka.group-id}",
        containerFactory = KafkaListenerConfig.SINGLE_THREAD_EQUIPMENT_KAFKA_LISTENER_CONTAINER_FACTORY
)
public class EquipmentMultiTopicMessageListener {

    private final ReferenceEquipmentService referenceEquipmentService;

    @KafkaHandler
    public void handle(Ammunition message) {
        log.info("[equipment] Ammunition received: {}", message);

        SaveReferenceEquipmentCommand command = SaveReferenceEquipmentCommand.builder()
                .equipmentId(message.getId())
                .equipmentType(ProductEquipmentType.AMMUNITION)
                .enabled(message.getEnabled())
                .name(message.getName())
                .build();

        saveReferenceEquipment(command);
    }

    @KafkaHandler
    public void handle(Attachment message) {
        log.info("[equipment] Attachment received: {}", message);

        SaveReferenceEquipmentCommand command = SaveReferenceEquipmentCommand.builder()
                .equipmentId(message.getId())
                .equipmentType(ProductEquipmentType.ATTACHMENT)
                .enabled(message.getEnabled())
                .name(message.getName())
                .build();

        saveReferenceEquipment(command);
    }

    @KafkaHandler
    public void handle(Grenade message) {
        log.info("[equipment] Grenade received: {}", message);

        SaveReferenceEquipmentCommand command = SaveReferenceEquipmentCommand.builder()
                .equipmentId(message.getId())
                .equipmentType(ProductEquipmentType.GRENADE)
                .enabled(message.getEnabled())
                .name(message.getName())
                .build();

        saveReferenceEquipment(command);
    }

    @KafkaHandler
    public void handle(Gun message) {
        log.info("[equipment] Gun received: {}", message);

        SaveReferenceEquipmentCommand command = SaveReferenceEquipmentCommand.builder()
                .equipmentId(message.getId())
                .equipmentType(ProductEquipmentType.GUN)
                .enabled(message.getEnabled())
                .name(message.getName())
                .build();

        saveReferenceEquipment(command);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object message) {
        log.warn("[equipment] Unknown message type received: {}", message);
        throw new IllegalArgumentException("Unknown message type: " + message.getClass());
    }

    private void saveReferenceEquipment(SaveReferenceEquipmentCommand command) {
        try {
            ReferenceEquipment equipment = referenceEquipmentService.save(command);
            log.info("[equipment] Reference equipment {} saved successfully", equipment.getId());
        } catch (Exception e) {
            log.error("[equipment] Failed to save reference equipment: {}", command, e);
            throw e;
        }
    }
}
