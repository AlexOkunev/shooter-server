package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = DateMapper.class
)
public interface PaginationInfoDtoMapper {

    PaginationInfo toDto(ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationInfo pageInfo);
}
