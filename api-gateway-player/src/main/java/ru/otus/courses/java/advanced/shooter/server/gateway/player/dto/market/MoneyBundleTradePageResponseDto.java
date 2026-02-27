package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationInfo;

import java.util.List;

public class MoneyBundleTradePageResponseDto extends PageResponseDto<MoneyBundleTradeDto> {
    public MoneyBundleTradePageResponseDto(List<MoneyBundleTradeDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}