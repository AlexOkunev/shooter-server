package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationInfo;

import java.util.List;

public class MoneyBundlePageResponseDto extends PageResponseDto<MoneyBundleDto> {
    public MoneyBundlePageResponseDto(List<MoneyBundleDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}