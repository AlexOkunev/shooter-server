package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentWritableData;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentsFilter;

import java.util.Collection;

public interface AttachmentService {

    Mono<AttachmentInfo> getOne(int id);

    Mono<AttachmentInfoListPage> search(AttachmentsFilter requestFilter, PaginationRequest paginationRequest);

    Mono<AttachmentInfoListPage> fetchByIds(Collection<Integer> ids);

    Mono<AttachmentInfo> create(AttachmentWritableData data);

    Mono<AttachmentInfo> update(int id, AttachmentWritableData data);
}
