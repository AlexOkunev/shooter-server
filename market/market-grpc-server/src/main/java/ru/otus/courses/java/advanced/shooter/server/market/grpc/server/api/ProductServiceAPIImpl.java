package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.ProductProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

@GRpcService
@RequiredArgsConstructor
public class ProductServiceAPIImpl extends ProductServiceAPIGrpc.ProductServiceAPIImplBase {

    private final ProductService productService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final ProductProtoMapper productProtoMapper;

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<ProductInfo> responseObserver) {
        Product product = productService.getProduct(request.getId());
        ProductInfo response = productProtoMapper.toResponse(product);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledProduct(GetProductRequest request, StreamObserver<ProductInfo> responseObserver) {
        Product product = productService.getEnabledProduct(request.getId());
        ProductInfo response = productProtoMapper.toResponse(product);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProducts(GetProductsRequest request, StreamObserver<ProductInfoListPage> responseObserver) {
        ProductFilterParams filterParams = productProtoMapper.toFilterParams(request);
        Pageable pageable = productProtoMapper.toPageable(request);

        Page<Product> data = productService.getProducts(filterParams, pageable);

        ProductInfoListPage response = ProductInfoListPage.newBuilder()
                .addAllData(productProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<ProductInfo> responseObserver) {
        ProductSavedData data = productProtoMapper.toSavedData(request);
        Product product = productService.createProduct(data);
        ProductInfo response = productProtoMapper.toResponse(product);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateProduct(UpdateProductRequest request, StreamObserver<ProductInfo> responseObserver) {
        ProductSavedData data = productProtoMapper.toSavedData(request);
        Product product = productService.updateProduct(request.getProductId(), request.getVersion(), data);
        ProductInfo response = productProtoMapper.toResponse(product);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
