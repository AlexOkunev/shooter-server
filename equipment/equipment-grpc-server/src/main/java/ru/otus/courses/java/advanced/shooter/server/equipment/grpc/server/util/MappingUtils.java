package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;

@UtilityClass
public class MappingUtils {
    public static long toMilliseconds(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }
}
