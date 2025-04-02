package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentReducedInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentReducedMapper {
    AttachmentReducedInfo toReducedResponse(Attachment source);
}
