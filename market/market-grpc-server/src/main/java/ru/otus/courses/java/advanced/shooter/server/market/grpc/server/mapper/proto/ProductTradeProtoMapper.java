package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.GetProductTradesRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;

import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                EquipmentTypeProtoMapper.class,
                ProductTradeStatusProtoMapper.class,
                DateMapper.class
        }
)
public abstract class ProductTradeProtoMapper {

    @Mappings({
            @Mapping(
                    target = "equipmentType",
                    source = ProductTrade.Fields.productEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentType
            ),
            @Mapping(
                    target = "equipmentId",
                    source = ProductTrade.Fields.productEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentId
            )
    })
    public abstract ProductTradeInfo toResponse(ProductTrade source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<ProductTradeInfo> toResponseList(Iterable<ProductTrade> source);

    public Pageable toPageable(GetProductTradesRequest request) {
        Sort sort = Sort.by(
                Sort.Order.asc(ProductTrade.Fields.playerUuid),
                Sort.Order.asc(ProductTrade.Fields.createdTimestamp)
        );

        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, sort);
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                sort
        );
    }

    public ProductTradeFilterParams toFilterParams(GetProductTradesRequest request) {
        if (!request.hasFilter()) {
            return ProductTradeFilterParams.builder()
                    .playerUuid(UUID.fromString(request.getPlayerUuid()))
                    .build();
        }

        return toFilterParamsInternal(request.getFilter(), request.getPlayerUuid());
    }

    protected abstract ProductTradeFilterParams toFilterParamsInternal(GetProductTradesRequest.Filter request, String playerUuid);
}