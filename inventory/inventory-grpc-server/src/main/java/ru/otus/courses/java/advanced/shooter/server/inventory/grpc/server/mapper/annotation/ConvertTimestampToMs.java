package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "timestamp", expression = "java(ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.util.MappingUtils.toMilliseconds(source.getTimestamp()))")
public @interface ConvertTimestampToMs {
}
