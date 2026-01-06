package ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CommonMapper {
    interface NamedMethods {
        String NAMED_TRIM = "trim";
    }

    @Named(NamedMethods.NAMED_TRIM)
    default String trim(String s) {
        return StringUtils.trim(s);
    }
}