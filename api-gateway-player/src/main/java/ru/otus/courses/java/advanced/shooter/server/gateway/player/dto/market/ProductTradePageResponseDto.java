package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationInfo;

import java.util.List;

public class ProductTradePageResponseDto extends PageResponseDto<ProductTradeDto> {
    public ProductTradePageResponseDto(List<ProductTradeDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}