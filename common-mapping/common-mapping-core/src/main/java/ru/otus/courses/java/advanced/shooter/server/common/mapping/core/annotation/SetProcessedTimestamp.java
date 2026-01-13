package ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(
        target = "processedTimestamp",
        expression = "java(java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC))"
)
public @interface SetProcessedTimestamp {
}