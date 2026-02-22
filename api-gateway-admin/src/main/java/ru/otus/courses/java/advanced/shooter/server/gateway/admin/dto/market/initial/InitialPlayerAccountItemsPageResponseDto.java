package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.market.initial;

import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class InitialPlayerAccountItemsPageResponseDto extends PageResponseDto<InitialPlayerAccountItemDto> {

    public InitialPlayerAccountItemsPageResponseDto(List<InitialPlayerAccountItemDto> items,
                                                    PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
