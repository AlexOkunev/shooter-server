package ru.otus.courses.java.advanced.shooter.server.gateway.player.handler;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.exception.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(CurrencyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle(CurrencyNotFoundException ex) {
        return Map.of(
                "status", 404,
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(GrenadeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle(GrenadeNotFoundException ex) {
        return Map.of(
                "status", 404,
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(GunNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle(GunNotFoundException ex) {
        return Map.of(
                "status", 404,
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(AmmunitionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle(AmmunitionNotFoundException ex) {
        return Map.of(
                "status", 404,
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(AttachmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle(AttachmentNotFoundException ex) {
        return Map.of(
                "status", 404,
                "error", "Not Found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleGrpc(StatusRuntimeException ex) {
        Status status = Status.fromThrowable(ex);

        HttpStatus httpStatus = grpcToHttp(status.getCode());

        return ResponseEntity
                .status(httpStatus)
                .body(Map.of(
                        "status", httpStatus.value(),
                        "error", httpStatus.getReasonPhrase(),
                        "grpcStatus", status.getCode().name(),
                        "message", extractGrpcMessage(ex, status)
                ));
    }

    private static HttpStatus grpcToHttp(Status.Code code) {
        return switch (code) {
            case INVALID_ARGUMENT, OUT_OF_RANGE -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case PERMISSION_DENIED -> HttpStatus.FORBIDDEN;
            case UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
            case FAILED_PRECONDITION -> HttpStatus.PRECONDITION_FAILED;
            case RESOURCE_EXHAUSTED -> HttpStatus.TOO_MANY_REQUESTS;
            case CANCELLED -> HttpStatus.REQUEST_TIMEOUT;
            case DEADLINE_EXCEEDED -> HttpStatus.GATEWAY_TIMEOUT;
            case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case UNIMPLEMENTED -> HttpStatus.NOT_IMPLEMENTED;
            case INTERNAL, DATA_LOSS, UNKNOWN -> HttpStatus.BAD_GATEWAY;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private static String extractGrpcMessage(StatusRuntimeException ex, Status status) {
        if (status.getDescription() != null && !status.getDescription().isBlank()) {
            return status.getDescription();
        }

        return ex.getMessage();
    }
}
