package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public class Constants {
    public static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, 10);
}
