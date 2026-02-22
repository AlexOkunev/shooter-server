package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaginationRequestMapper {

    PaginationRequest toProto(PaginationRequestDto dto);
}
