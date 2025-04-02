package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.common.Common;

@UtilityClass
public class PaginationUtils {
    public static Pageable getPageable(Common.PaginationRequest paginationRequest, Sort sort) {
        return PageRequest.of(paginationRequest.getPage(), paginationRequest.getCount(), sort);
    }

    public static Pageable getPageable(int page, int count, Sort sort) {
        return PageRequest.of(page, count, sort);
    }
}
