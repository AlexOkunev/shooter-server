package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class ProductPageResponseDto extends PageResponseDto<ProductDto> {
    public ProductPageResponseDto(List<ProductDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
