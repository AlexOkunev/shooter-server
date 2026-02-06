package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceEquipmentCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class ReferenceEquipmentMapper {

    @Mapping(
            target = ReferenceEquipment.Fields.id,
            source = "."
    )
    public abstract ReferenceEquipment toEntity(SaveReferenceEquipmentCommand command);

    protected abstract ReferenceEquipmentId toEquipmentId(SaveReferenceEquipmentCommand command);
}
