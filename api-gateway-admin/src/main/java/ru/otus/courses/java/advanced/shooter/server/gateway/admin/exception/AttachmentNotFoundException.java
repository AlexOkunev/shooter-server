package ru.otus.courses.java.advanced.shooter.server.gateway.admin.exception;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException(int id) {
        super("Attachment not found: %d".formatted(id));
    }
}
