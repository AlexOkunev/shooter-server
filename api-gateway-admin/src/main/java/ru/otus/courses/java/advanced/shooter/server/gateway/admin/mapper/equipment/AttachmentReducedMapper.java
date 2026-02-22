package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentReducedInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentReducedDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationInfoDtoMapper;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                PaginationInfoDtoMapper.class
        }
)
public abstract class AttachmentReducedMapper {

    public abstract AttachmentReducedDto toDto(AttachmentReducedInfo attachmentInfo);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<AttachmentReducedDto> toDtoList(Collection<AttachmentReducedInfo> attachmentInfos);
}
