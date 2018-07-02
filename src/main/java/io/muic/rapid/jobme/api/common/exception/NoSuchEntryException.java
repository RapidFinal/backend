package io.muic.rapid.jobme.api.common.exception;

public class NoSuchEntryException extends RuntimeException {
    public NoSuchEntryException() {
    }

    public NoSuchEntryException(String message) {
        super(message);
    }

    public NoSuchEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEntryException(Throwable cause) {
        super(cause);
    }

    public NoSuchEntryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
