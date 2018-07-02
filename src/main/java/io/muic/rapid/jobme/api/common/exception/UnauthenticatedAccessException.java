package io.muic.rapid.jobme.api.common.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthenticatedAccessException extends AuthenticationException {
    public UnauthenticatedAccessException(String msg, Throwable t) {
        super(msg, t);
    }

    public UnauthenticatedAccessException(String msg) {
        super(msg);
    }
}
