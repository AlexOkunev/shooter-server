package ru.otus.courses.java.advanced.shooter.server.gateway.player.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record GrpcBlockingPoolProperties(@Positive int maxThreads,
                                         @Positive int maxQueuedTasks,
                                         @NotBlank String threadNamePrefix
) {
}
