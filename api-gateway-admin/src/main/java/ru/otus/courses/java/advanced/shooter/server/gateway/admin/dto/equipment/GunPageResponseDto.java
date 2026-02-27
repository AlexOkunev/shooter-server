package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;


import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class GunPageResponseDto extends PageResponseDto<GunDto> {
    public GunPageResponseDto(List<GunDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
