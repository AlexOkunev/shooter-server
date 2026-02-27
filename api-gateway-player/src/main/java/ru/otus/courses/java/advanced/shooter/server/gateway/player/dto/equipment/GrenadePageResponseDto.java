package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment;


import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationInfo;

import java.util.List;

public class GrenadePageResponseDto extends PageResponseDto<GrenadeDto> {
    public GrenadePageResponseDto(List<GrenadeDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
