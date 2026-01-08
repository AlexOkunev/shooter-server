package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentType;

@Converter
public class AttachmentTypeConverter implements AttributeConverter<AttachmentType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AttachmentType attachmentType) {
        return attachmentType.getCode();
    }

    @Override
    public AttachmentType convertToEntityAttribute(Integer code) {
        return AttachmentType.fromCode(code);
    }
}
