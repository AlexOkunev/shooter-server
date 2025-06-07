package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

@GRpcService
@RequiredArgsConstructor
public class ProductServiceAPIImpl extends ProductServiceAPIGrpc.ProductServiceAPIImplBase {
    private final ProductService productService;

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<ProductInfo> responseObserver) {
        responseObserver.onNext(productService.getProduct(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getProducts(GetProductsRequest request, StreamObserver<ProductInfoListPage> responseObserver) {
        responseObserver.onNext(productService.getProducts(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<ProductInfo> responseObserver) {
        responseObserver.onNext(productService.createProduct(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateProduct(UpdateProductRequest request, StreamObserver<ProductInfo> responseObserver) {
        responseObserver.onNext(productService.updateProduct(request));
        responseObserver.onCompleted();
    }
}
