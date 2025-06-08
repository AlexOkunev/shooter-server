package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "createdTimestamp", expression = "java(ru.otus.courses.java.advanced.shooter.server.common.utils.mapping.MappingUtils.toMilliseconds(source.getCreatedTimestamp()))")
public @interface ConvertCreatedTimestampToMs {
}
