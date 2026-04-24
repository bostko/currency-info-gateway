package net.isbg.currency.xmlapi.exception;

public class DuplicateRequestException extends RuntimeException {
    public DuplicateRequestException(String requestId) {
        super("Duplicate requestId: " + requestId);
    }
}