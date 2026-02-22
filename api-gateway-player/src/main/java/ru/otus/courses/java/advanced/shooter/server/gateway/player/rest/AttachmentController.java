package ru.otus.courses.java.advanced.shooter.server.gateway.player.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AttachmentSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.AttachmentMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationRequestMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.service.equipment.AttachmentService;

@Slf4j
@Tag(name = "Equipment / Attachment API")
@RestController
@RequestMapping("/equipment/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;
    private final PaginationRequestMapper paginationRequestMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get attachment")
    public Mono<AttachmentDto> getOne(@AuthenticationPrincipal Jwt jwt, @PathVariable int id) {
        return attachmentService.getOne(id)
                .map(attachmentMapper::toDto);
    }

    @PostMapping
    @Operation(summary = "Search attachments by filter")
    public Mono<PageResponseDto<AttachmentDto>> search(@RequestBody @NotNull @Valid AttachmentSearchRequestDto request) {
        return attachmentService.search(
                        attachmentMapper.toProto(request),
                        paginationRequestMapper.toProto(request.getPaginationRequest())
                )
                .map(attachmentMapper::toPageDto);
    }
}
