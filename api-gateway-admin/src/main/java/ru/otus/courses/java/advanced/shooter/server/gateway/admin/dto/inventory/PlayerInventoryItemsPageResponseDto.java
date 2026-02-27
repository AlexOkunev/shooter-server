package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class PlayerInventoryItemsPageResponseDto extends PageResponseDto<PlayerInventoryItemDto> {
    public PlayerInventoryItemsPageResponseDto(List<PlayerInventoryItemDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
