package ru.otus.courses.java.advanced.shooter.server.gateway.admin.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.ProductDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.ProductPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.SaveProductRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.ProductMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.CurrencyMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.market.context.factory.EquipmentMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.ProductService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;

@Slf4j
@Tag(name = "Market / Product API")
@RestController
@RequestMapping("/market/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyMappingContextFactory currencyMappingContextFactory;
    private final EquipmentMappingContextFactory equipmentMappingContextFactory;

    @PostMapping
    @Operation(summary = "Create product")
    public Mono<ProductDto> create(@RequestBody @Valid @NotNull SaveProductRequestDto requestDto) {
        Mono<ProductInfo> productInfoMono = productService.create(productMapper.toProto(requestDto))
                .cache();

        return Mono.zip(
                        productInfoMono,
                        currencyMappingContextFactory.createForProduct(productInfoMono),
                        equipmentMappingContextFactory.createForProduct(productInfoMono)
                )
                .map(tuple -> productMapper.toDto(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    public Mono<ProductDto> fetchOne(@PathVariable int id) {
        Mono<ProductInfo> moneyBundleInfoMono = productService.fetchOne(id).cache();

        return Mono.zip(
                        moneyBundleInfoMono,
                        currencyMappingContextFactory.createForProduct(moneyBundleInfoMono),
                        equipmentMappingContextFactory.createForProduct(moneyBundleInfoMono)
                )
                .map(tuple ->
                        productMapper.toDto(tuple.getT1(), tuple.getT2(), tuple.getT3())
                );
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product")
    public Mono<ProductDto> update(
            @Parameter(description = "Money bundle ID") @PathVariable int productId,
            @RequestBody @Valid @NotNull SaveProductRequestDto requestDto,
            @Parameter(description = "Optimistic lock version") @RequestParam int version
    ) {
        Mono<ProductInfo> productInfoMono = productService.update(
                        productId,
                        version,
                        productMapper.toProto(requestDto)
                )
                .cache();

        return Mono.zip(
                        productInfoMono,
                        currencyMappingContextFactory.createForProduct(productInfoMono),
                        equipmentMappingContextFactory.createForProduct(productInfoMono)
                )
                .map(tuple -> productMapper.toDto(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    @GetMapping
    @Operation(summary = "Get products list page")
    public Mono<ProductPageResponseDto> fetchPage(
            @ModelAttribute @NotNull @Valid PaginationRequestDto paginationRequest
    ) {
        PaginationRequest paginationRequestProto = paginationRequestMapper.toProto(paginationRequest);

        Mono<ProductInfoListPage> productInfoListPageMono = productService.fetchProductsPage(paginationRequestProto)
                .cache();

        return Mono.zip(
                        productInfoListPageMono,
                        currencyMappingContextFactory.createForProducts(productInfoListPageMono),
                        equipmentMappingContextFactory.createForProducts(productInfoListPageMono)
                )
                .map(tuple ->
                        productMapper.toPageDto(tuple.getT1(), tuple.getT2(), tuple.getT3())
                );
    }
}
