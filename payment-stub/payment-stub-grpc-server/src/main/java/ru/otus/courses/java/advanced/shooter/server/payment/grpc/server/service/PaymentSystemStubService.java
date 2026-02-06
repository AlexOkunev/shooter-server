package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service;

public interface PaymentSystemStubService {

    String getSession(String playerUuid, String playerEmail, int rublesAmount, String tradeUuid);

    String getPublicToken(String session);
}
