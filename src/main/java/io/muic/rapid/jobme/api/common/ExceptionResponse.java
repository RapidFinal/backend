package io.muic.rapid.jobme.api.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class ExceptionResponse {
    private final Integer status;
    private String message;
    private String path;
    private String method;
    // TODO: create constructor


    public ExceptionResponse(HttpServletRequest req, Exception ex, HttpStatus httpStatus ) {
        this.status = httpStatus.value();
        this.message = ex.getMessage();
        this.path = req.getContextPath() + req.getServletPath();
        this.method = req.getMethod();
    }
}