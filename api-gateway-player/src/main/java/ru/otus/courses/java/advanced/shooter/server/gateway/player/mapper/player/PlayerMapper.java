package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.player;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.common.page.PageResponseDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.player.PlayerDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.player.PlayersSearchRequestDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayersFilter;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                PaginationInfoDtoMapper.class
        }
)
public abstract class PlayerMapper {

    @Mapping(source = "createdTimestamp", target = "createdAt")
    public abstract PlayerDto toDto(PlayerInfo playerInfo);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<PlayerDto> toDtoList(Collection<PlayerInfo> playerInfos);

    @Mapping(source = "data", target = "items")
    public abstract PageResponseDto<PlayerDto> toPageDto(PlayerInfoListPage playerInfoListPage);

    @Mapping(target = "enabled", constant = "true")
    public abstract PlayersFilter toProto(PlayersSearchRequestDto playersSearchRequestDto);
}
