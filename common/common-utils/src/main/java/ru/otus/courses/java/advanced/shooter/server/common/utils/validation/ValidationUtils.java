package ru.otus.courses.java.advanced.shooter.server.common.utils.validation;

import lombok.experimental.UtilityClass;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;

@UtilityClass
public class ValidationUtils {
    public static void validatePaginationRequest(PaginationRequest paginationRequest) {
        if (paginationRequest.getPage() < 0) {
            throw new InvalidRequestException("Page number must be greater than or equal to zero");
        }

        if (paginationRequest.getCount() < 1) {
            throw new InvalidRequestException("Page size cannot be less than 1");
        }
    }
}
