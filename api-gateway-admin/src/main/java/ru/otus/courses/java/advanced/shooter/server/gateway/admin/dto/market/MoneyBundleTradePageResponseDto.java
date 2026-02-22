package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleTradeDto;

import java.util.List;

public class MoneyBundleTradePageResponseDto extends PageResponseDto<MoneyBundleTradeDto> {
    public MoneyBundleTradePageResponseDto(List<MoneyBundleTradeDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
//TODO create such wrappers for all pages
