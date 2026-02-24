package ru.otus.courses.java.advanced.shooter.server.gateway.player.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.ProductDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market.ProductPageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.ProductWithDtoContextMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.CurrencyDtoCachingMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.market.context.factory.EquipmentDtoCachingMappingContextFactory;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market.ProductService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;

@Slf4j
@Tag(name = "Market / Product API")
@RestController
@RequestMapping("/market/products")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "caches.enabled", havingValue = "true")
public class ProductCachingController {

    private final ProductService productService;
    private final ProductWithDtoContextMapper productMapper;
    private final PaginationRequestMapper paginationRequestMapper;
    private final CurrencyDtoCachingMappingContextFactory currencyMappingContextFactory;
    private final EquipmentDtoCachingMappingContextFactory equipmentMappingContextFactory;

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    public Mono<ProductDto> fetchOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
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

    @GetMapping
    @Operation(summary = "Get products list page")
    public Mono<ProductPageResponseDto> fetchPage(
            @AuthenticationPrincipal Jwt jwt,
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
