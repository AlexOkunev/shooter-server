package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.inventory.initial;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class InitialPlayerInventoryItemsPageResponseDto extends PageResponseDto<InitialPlayerInventoryItemDto> {
    public InitialPlayerInventoryItemsPageResponseDto(List<InitialPlayerInventoryItemDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}