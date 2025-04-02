package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "createdTimestamp", expression = "java(ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.MappingUtils.toMilliseconds(source.getCreatedTimestamp()))")
@Mapping(target = "updatedTimestamp", expression = "java(ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util.MappingUtils.toMilliseconds(source.getUpdatedTimestamp()))")
public @interface ConvertTimestampsToMs {
}
