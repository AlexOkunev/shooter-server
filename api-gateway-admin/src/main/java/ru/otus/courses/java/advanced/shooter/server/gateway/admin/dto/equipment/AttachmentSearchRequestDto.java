package ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.common.page.PaginationRequestDto;

import java.time.ZonedDateTime;
import java.util.List;

@Schema(
        name = "AttachmentSearchRequestDto",
        description = "Filter and pagination request for attachments"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentSearchRequestDto {

    @Schema(description = "Attachment IDs", example = "[1, 2, 3]")
    private List<Integer> attachmentIds;

    @Schema(description = "Name prefix", example = "Red")
    private String name;

    @Schema(description = "Attachment type", example = "SCOPE")
    private AttachmentType type;

    @Schema(description = "Compatible gun IDs", example = "[10, 11]")
    private List<Integer> compatibleGunIds;

    @Schema(
            description = "Filter by compatible guns that must enabled",
            example = "true"
    )
    private Boolean onlyEnabledCompatibleGuns;

    @Schema(
            description = "Return attachments updated after given time",
            example = "2026-02-17T12:34:56+01:00"
    )
    private ZonedDateTime updatedAfter;

    @Schema(description = "Pagination request")
    @NotNull
    @Valid
    private PaginationRequestDto paginationRequest;
}
