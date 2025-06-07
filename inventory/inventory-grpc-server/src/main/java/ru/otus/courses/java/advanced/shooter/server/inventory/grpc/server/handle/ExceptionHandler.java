package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.handle;

import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;

@Slf4j
@GRpcServiceAdvice
public class ExceptionHandler {
    @GRpcExceptionHandler
    public Status handleException(ObjectNotFoundException e, GRpcExceptionScope scope) {
        return Status.NOT_FOUND
                .withDescription("Resource not found")
                .augmentDescription(e.getMessage());
    }

    @GRpcExceptionHandler
    public Status handleException(InvalidRequestException e, GRpcExceptionScope scope) {
        return Status.INVALID_ARGUMENT
                .withDescription("Invalid request")
                .augmentDescription(e.getMessage());
    }

    @GRpcExceptionHandler
    public Status handleException(DataIntegrityViolationException e, GRpcExceptionScope scope) {
        if (StringUtils.containsAnyIgnoreCase(e.getMessage(), "duplicate key", "already exists")) {
            return Status.ALREADY_EXISTS
                    .withDescription("Unique constraint violated");
        } else {
            return Status.INTERNAL
                    .withDescription(e.getMessage());
        }
    }

    @GRpcExceptionHandler
    public Status handleException(RuntimeException e, GRpcExceptionScope scope) {
        log.error(e.getMessage(), e);
        return Status.INTERNAL
                .withDescription("Internal server error")
                .augmentDescription(e.getMessage());
    }
}
