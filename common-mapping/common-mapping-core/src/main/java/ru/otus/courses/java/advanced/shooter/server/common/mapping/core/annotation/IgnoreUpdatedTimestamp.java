package ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(
        target = "updatedTimestamp",
        ignore = true
)
public @interface IgnoreUpdatedTimestamp {
}
