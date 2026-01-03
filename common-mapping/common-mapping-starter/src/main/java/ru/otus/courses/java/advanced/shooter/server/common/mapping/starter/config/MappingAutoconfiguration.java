package ru.otus.courses.java.advanced.shooter.server.common.mapping.starter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;

@Configuration
@ComponentScan(basePackageClasses = CommonMapper.class)
public class MappingAutoconfiguration {
}
