package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import lombok.experimental.UtilityClass;
import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                CommonMapper.class,
                EquipmentTypeProtoMapper.class
        }
)
public abstract class ProductProtoMapper {

    @UtilityClass
    private static final class ProtoFields {
        private static final String TARGET_PRICE = "price";
        private static final String TARGET_EQUIPMENT = "equipment";

        private static final String TARGET_CURRENCY_ID = "currencyId";
        private static final String TARGET_AMOUNT = "amount";

        private static final String TARGET_EQUIPMENT_ID = "equipmentId";
        private static final String TARGET_EQUIPMENT_TYPE = "equipmentType";

        private static final String SOURCE_EQUIPMENT_EQUIPMENT_TYPE = "equipment.equipmentType";
        private static final String SOURCE_EQUIPMENT_EQUIPMENT_ID = "equipment.equipmentId";
        private static final String SOURCE_EQUIPMENT_AMOUNT = "equipment.amount";
        private static final String SOURCE_PRICE_AMOUNT = "price.amount";
        private static final String SOURCE_PRICE_CURRENCY_ID = "price.currencyId";
    }

    @Mappings({
            @Mapping(
                    target = ProtoFields.TARGET_PRICE,
                    source = "."
            ),
            @Mapping(
                    target = ProtoFields.TARGET_EQUIPMENT,
                    source = "."
            )
    })
    public abstract ProductInfo toResponse(Product source);

    @Mappings({
            @Mapping(
                    target = ProtoFields.TARGET_CURRENCY_ID,
                    source = Product.Fields.priceCurrencyId
            ),
            @Mapping(
                    target = ProtoFields.TARGET_AMOUNT,
                    source = Product.Fields.price
            )
    })
    protected abstract PriceInfo toPriceInfo(Product source);

    @Mappings({
            @Mapping(
                    target = ProtoFields.TARGET_EQUIPMENT_ID,
                    source = Product.Fields.equipmentId + "." + ReferenceEquipmentId.Fields.equipmentId
            ),
            @Mapping(
                    target = ProtoFields.TARGET_EQUIPMENT_TYPE,
                    source = Product.Fields.equipmentId + "." + ReferenceEquipmentId.Fields.equipmentType
            ),
            @Mapping(
                    target = ProtoFields.TARGET_AMOUNT,
                    source = Product.Fields.equipmentAmount
            )
    })
    protected abstract ProductEquipmentInfo toProductEquipmentInfo(Product source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<ProductInfo> toResponseList(Iterable<Product> source);

    public ProductSavedData toSavedData(CreateProductRequest request) {
        if (!request.hasData()) {
            return null;
        }

        return toSavedDataInternal(request.getData());
    }

    public ProductSavedData toSavedData(UpdateProductRequest request) {
        if (!request.hasData()) {
            return null;
        }

        return toSavedDataInternal(request.getData());
    }

    public ProductFilterParams toFilterParams(GetProductsRequest request) {
        if (!request.hasFilter()) {
            return ProductFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    public Pageable toPageable(GetProductsRequest request) {
        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"));
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    @Mappings({
            @Mapping(
                    target = ProductSavedData.Fields.equipmentType,
                    source = ProtoFields.SOURCE_EQUIPMENT_EQUIPMENT_TYPE
            ),
            @Mapping(
                    target = ProductSavedData.Fields.equipmentId,
                    source = ProtoFields.SOURCE_EQUIPMENT_EQUIPMENT_ID
            ),
            @Mapping(
                    target = ProductSavedData.Fields.equipmentAmount,
                    source = ProtoFields.SOURCE_EQUIPMENT_AMOUNT
            ),
            @Mapping(
                    target = ProductSavedData.Fields.priceCurrencyAmount,
                    source = ProtoFields.SOURCE_PRICE_AMOUNT
            ),
            @Mapping(
                    target = ProductSavedData.Fields.priceCurrencyId,
                    source = ProtoFields.SOURCE_PRICE_CURRENCY_ID
            )
    })
    protected abstract ProductSavedData toSavedDataInternal(ProductWritableData source);

    protected abstract ProductFilterParams toFilterParamsInternal(GetProductsRequest.Filter filter);
}
