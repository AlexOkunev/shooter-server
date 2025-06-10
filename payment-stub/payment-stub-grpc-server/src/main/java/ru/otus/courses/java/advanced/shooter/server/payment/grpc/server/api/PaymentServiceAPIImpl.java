package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service.PaymentService;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.PaymentServiceAPIGrpc;

@GRpcService
@RequiredArgsConstructor
public class PaymentServiceAPIImpl extends PaymentServiceAPIGrpc.PaymentServiceAPIImplBase {

    private final PaymentService paymentService;

    @Override
    public void createPayment(CreatePaymentRequest request, StreamObserver<CreatePaymentResponse> responseObserver) {
        responseObserver.onNext(paymentService.createPayment(request));
        responseObserver.onCompleted();
    }
}