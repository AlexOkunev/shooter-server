package ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation;

import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@ValueMapping(
        source = "UNRECOGNIZED",
        target = MappingConstants.THROW_EXCEPTION
)
public @interface ThrowOnUnrecognized {
}