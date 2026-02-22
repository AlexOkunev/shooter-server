package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.PlayerAccountItemDto;

import java.util.List;

public class PlayerAccountItemPageResponseDto extends PageResponseDto<PlayerAccountItemDto> {
    public PlayerAccountItemPageResponseDto(List<PlayerAccountItemDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
//TODO create such wrappers for all pages
