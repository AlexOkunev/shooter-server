package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.MoneyBundleDto;

import java.util.List;

public class MoneyBundlePageResponseDto extends PageResponseDto<MoneyBundleDto> {
    public MoneyBundlePageResponseDto(List<MoneyBundleDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
//TODO create such wrappers for all pages
