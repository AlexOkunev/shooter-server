package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentWritableData;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentsFilter;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentSaveRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentSearchRequestDto;
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
                PaginationInfoDtoMapper.class,
                GunReducedMapper.class
        }
)
public abstract class AttachmentMapper {

    public abstract AttachmentDto toDto(AttachmentInfo attachmentInfo);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<AttachmentDto> toDtoList(Collection<AttachmentInfo> attachmentInfos);

    @Mapping(source = "data", target = "items")
    public abstract PageResponseDto<AttachmentDto> toPageDto(AttachmentInfoListPage attachmentInfoListPage);

    public abstract AttachmentsFilter toProto(AttachmentSearchRequestDto requestDto);

    public abstract AttachmentWritableData toProto(AttachmentSaveRequestDto requestDto);
}
