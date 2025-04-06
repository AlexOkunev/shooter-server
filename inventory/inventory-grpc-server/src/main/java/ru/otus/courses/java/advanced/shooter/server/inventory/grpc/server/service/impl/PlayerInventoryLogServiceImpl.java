package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryLogEntry;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.PlayerInventoryLogEntryMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.PlayerInventoryLogRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryLogService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.util.PaginationUtils;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.util.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

@Service
@RequiredArgsConstructor
public class PlayerInventoryLogServiceImpl implements PlayerInventoryLogService {
    private final PlayerInventoryLogRepository playerInventoryLogRepository;

    private final PlayerInventoryLogEntryMapper playerInventoryLogEntryMapper;

    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, PlayerInventoryLogEntry.Fields.timestamp);

    @Override
    public PlayerInventoryLogPage getPlayerInventoryLogPage(GetPlayerInventoryLogRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Pageable pageable = request.hasPaginationRequest() ?
                PaginationUtils.getPageable(request.getPaginationRequest(), defaultSort) :
                PaginationUtils.getPageable(0, 10, defaultSort);

        Page<PlayerInventoryLogEntry> data = playerInventoryLogRepository.findAllByPlayerId(request.getPlayerId(), pageable);

        return PlayerInventoryLogPage.newBuilder()
                .addAllData(data.map(playerInventoryLogEntryMapper::toResponse))
                .setTotalCount(data.getTotalElements())
                .build();
    }
}
