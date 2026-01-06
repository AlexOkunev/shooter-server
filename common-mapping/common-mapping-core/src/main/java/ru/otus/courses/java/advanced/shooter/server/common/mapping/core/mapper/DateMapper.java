package ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class DateMapper {
    public long toMilliseconds(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public ZonedDateTime toZonedDateTime(long timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }
}
