package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.AttachmentEffect;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class AttachmentEffectMapper {
    public abstract AttachmentEffect toEntity(AttachmentSavedData.AttachmentEffect source);
}
