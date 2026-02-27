package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class PlayerInventoryLogPageResponseDto extends PageResponseDto<PlayerInventoryLogEntryDto> {
    public PlayerInventoryLogPageResponseDto(List<PlayerInventoryLogEntryDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
