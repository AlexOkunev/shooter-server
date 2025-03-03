package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.mapper.PlayerMapper;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.repository.PlayerRepository;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.specifications.PlayerSpecifications;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.*;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.common.Common;

import java.util.ArrayList;
import java.util.List;

@GRpcService
@RequiredArgsConstructor
public class ShooterPlayersServiceAPIImpl extends ShooterPlayersServiceAPIGrpc.ShooterPlayersServiceAPIImplBase {
    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    @Override
    public void getPlayer(GetPlayerRequest request, StreamObserver<GetPlayerResponse> responseObserver) {
        GetPlayerResponse.Builder responseBuilder = GetPlayerResponse.newBuilder();

        playerRepository.findByPlayerId(request.getPlayerId())
                .ifPresentOrElse(
                        player -> responseBuilder.setPlayer(playerMapper.toResponse(player)),
                        () -> responseBuilder.setError(
                                Common.Error.newBuilder()
                                        .setErrorType(Common.Error.ErrorType.DATA_NOT_FOUND)
                                        .setMessage("Player '%d' not found".formatted(request.getPlayerId()))
                                        .build()));

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPlayers(GetPlayersRequest request, StreamObserver<GetPlayersResponse> responseObserver) {
        GetPlayersResponse.Builder responseBuilder = GetPlayersResponse.newBuilder();

        Specification<Player> specification = getPlayerSpecification(request);

        Pageable pageable = request.hasPaginationRequest()
                ? PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount())
                : PageRequest.of(0, 10);

        Page<Player> playersPage = playerRepository.findAll(specification, pageable);

        responseBuilder.setPage(
                PlayerInfoListPage.newBuilder()
                        .addAllData(playersPage.map(playerMapper::toResponse))
                        .setTotalCount(playersPage.getTotalPages())
                        .build());

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
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

        return specifications;
    }
}
