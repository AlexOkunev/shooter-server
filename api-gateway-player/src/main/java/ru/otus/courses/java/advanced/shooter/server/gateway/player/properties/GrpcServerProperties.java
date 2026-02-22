package ru.otus.courses.java.advanced.shooter.server.gateway.player.properties;


import jakarta.validation.constraints.NotBlank;

public record GrpcServerProperties(@NotBlank String host, String overrideAuthority, int port) {
}
