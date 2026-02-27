package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;


import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationInfo;

import java.util.List;

public class AttachmentPageResponseDto extends PageResponseDto<AttachmentDto> {
    public AttachmentPageResponseDto(List<AttachmentDto> items, PaginationInfo paginationInfo) {
        super(items, paginationInfo);
    }
}
