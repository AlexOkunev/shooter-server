package ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CommonMapper {
    interface NamedMethods {
        String NAMED_TRIM = "trim";
        String NAMED_STRING_TO_UUID = "stringToUUID";
    }

    @Named(NamedMethods.NAMED_TRIM)
    default String trim(String s) {
        return StringUtils.trim(s);
    }

    @Named(NamedMethods.NAMED_STRING_TO_UUID)
    default UUID stringToUUID(String s) {
        return UUID.fromString(s);
    }
}