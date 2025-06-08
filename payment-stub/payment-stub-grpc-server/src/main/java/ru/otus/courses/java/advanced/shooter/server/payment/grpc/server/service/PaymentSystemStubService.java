package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service;

public interface PaymentSystemStubService {
    String getSession(Integer playerId, String playerEmail, int rublesAmount, String tradeUuid);

    String getPublicToken(String session);
}
