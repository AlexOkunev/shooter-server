package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class PlayerAccountLogPageResponseDto extends PageResponseDto<PlayerAccountLogEntryDto> {
    public PlayerAccountLogPageResponseDto(List<PlayerAccountLogEntryDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
