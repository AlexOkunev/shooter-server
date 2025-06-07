package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaginationInfoMapper {
    @Mappings({
            @Mapping(source = "totalElements", target = "totalCount"),
            @Mapping(source = "totalPages", target = "totalPages"),
            @Mapping(source = "number", target = "currentPageNumber")
    })
    PaginationInfo toResponse(Page<?> page);
}
