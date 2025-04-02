package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.util;

import lombok.experimental.UtilityClass;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.common.Common;

@UtilityClass
public class ValidationUtils {
    public static void validatePaginationRequest(Common.PaginationRequest paginationRequest) {
        if (paginationRequest.getPage() < 0) {
            throw new InvalidRequestException("Page number must be greater than or equal to zero");
        }

        if (paginationRequest.getCount() < 1) {
            throw new InvalidRequestException("Page size cannot be less than 1");
        }
    }
}
