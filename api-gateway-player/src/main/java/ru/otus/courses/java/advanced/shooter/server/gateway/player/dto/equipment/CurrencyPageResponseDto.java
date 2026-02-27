package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment;


import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationInfo;

import java.util.List;

public class CurrencyPageResponseDto extends PageResponseDto<CurrencyDto> {
    public CurrencyPageResponseDto(List<CurrencyDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
