package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.mapper.PlayerMapper;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.repository.PlayerRepository;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.specifications.PlayerSpecifications;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.util.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    @Override
    public PlayerInfo getPlayer(GetPlayerRequest request) {
        return playerRepository.findByPlayerId(request.getPlayerId())
                .map(playerMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Player with id '%d' not found".formatted(request.getPlayerId())));
    }

    @Override
    public PlayerInfoListPage getPlayers(GetPlayersRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<Player> specification = getPlayerSpecification(request);

        Pageable pageable = request.hasPaginationRequest()
                ? PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), Sort.by(Sort.Direction.ASC, Player.Fields.playerId))
                : PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Player.Fields.playerId));

        Page<Player> playersPage = playerRepository.findAll(specification, pageable);

        return PlayerInfoListPage.newBuilder()
                .addAllData(playersPage.map(playerMapper::toResponse))
                .setTotalCount(playersPage.getTotalElements())
                .build();
    }

    private static Specification<Player> getPlayerSpecification(GetPlayersRequest request) {
        List<Specification<Player>> specifications = request.hasFilter() ?
                getPlayerSpecificationsFromFilter(request.getFilter()) : List.of();
        return Specification.allOf(specifications);
    }

    private static List<Specification<Player>> getPlayerSpecificationsFromFilter(PlayersFilter playersFilter) {
        List<Specification<Player>> specifications = new ArrayList<>();

        if (playersFilter.hasEmail()) {
            specifications.add(PlayerSpecifications.byEmailStartsWith(playersFilter.getEmail()));
        }

        if (playersFilter.hasLogin()) {
            specifications.add(PlayerSpecifications.byLoginStartsWith(playersFilter.getLogin()));
        }

        if (playersFilter.hasEnabled()) {
            specifications.add(PlayerSpecifications.byEnabled(playersFilter.getEnabled()));
        }

        if (playersFilter.getPlayerIdCount() > 0) {
            specifications.add(PlayerSpecifications.byPlayerIds(playersFilter.getPlayerIdList()));
        }

        return specifications;
    }
}
