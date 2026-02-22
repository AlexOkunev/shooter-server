package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory;

import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationInfo;

import java.util.List;

public class PlayerInventoryItemsPageResponseDto extends PageResponseDto<PlayerInventoryItemDto> {
    public PlayerInventoryItemsPageResponseDto(List<PlayerInventoryItemDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
//TODO create such wrappers for all pages
