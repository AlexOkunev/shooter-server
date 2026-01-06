package ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mappings({
        @Mapping(
                target = "id",
                ignore = true
        ),
        @Mapping(
                target = "createdTimestamp",
                expression = "java(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC))"
        ),
        @Mapping(
                target = "updatedTimestamp",
                expression = "java(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC))"
        )
})
public @interface CreateEntityMapping {
}
