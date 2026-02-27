package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.player;

import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PaginationInfo;

import java.util.List;

public class PlayerPageResponseDto extends PageResponseDto<PlayerDto> {
    public PlayerPageResponseDto(List<PlayerDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
