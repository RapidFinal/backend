package io.muic.rapid.jobme.api.common;

import io.muic.rapid.jobme.api.common.exception.DuplicateEntryException;
import io.muic.rapid.jobme.api.common.exception.ForbiddenAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(ForbiddenAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    ExceptionResponse forbidden(HttpServletRequest req, Exception ex){
        return  new ExceptionResponse(req,ex, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    ExceptionResponse duplicateEntry(HttpServletRequest req, Exception ex){
        return new ExceptionResponse(req,ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ExceptionResponse invalidEntry(HttpServletRequest req, Exception ex){
        return new ExceptionResponse(req,ex, HttpStatus.BAD_REQUEST);
    }
}
