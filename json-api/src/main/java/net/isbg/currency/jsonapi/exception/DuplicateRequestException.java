package net.isbg.currency.jsonapi.exception;

public class DuplicateRequestException extends RuntimeException {
    public DuplicateRequestException(String requestId) {
        super("Duplicate requestId: " + requestId);
    }
}