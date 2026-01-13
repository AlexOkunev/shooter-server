package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain;

import lombok.experimental.UtilityClass;
import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetProcessedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ProcessedExternalMessage;
import ru.otus.courses.java.advanced.shooter.server.market.outbox.ProductTradeIssueRequiredMessage;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                CommonMapper.class,
                DateMapper.class
        }
)
public abstract class ExternalMessageMapper {

    @UtilityClass
    private static final class SourceAvroFields {
        public static final String FIELD_MESSAGE_UUID = "messageUuid";
        public static final String FIELD_CREATED_TIMESTAMP = "createdTimestamp";
    }

    @SetProcessedTimestamp
    @Mappings({
            @Mapping(
                    target = ProcessedExternalMessage.Fields.uuid,
                    source = SourceAvroFields.FIELD_MESSAGE_UUID,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_STRING_TO_UUID
            ),
            @Mapping(
                    target = ProcessedExternalMessage.Fields.createdTimestamp,
                    source = SourceAvroFields.FIELD_CREATED_TIMESTAMP
            )
    })
    public abstract ProcessedExternalMessage map(ProductTradeIssueRequiredMessage source);
}
