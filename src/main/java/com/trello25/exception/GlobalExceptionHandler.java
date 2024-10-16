package com.trello25.exception;

import com.trello25.common.SlackEvent;
import com.trello25.common.UnHandledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unHandledException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(500, e.getMessage());

        eventPublisher.publishEvent(new UnHandledEvent(String.format("<!channel> \"\"\" %s \"\"\"", e.getMessage())));
        
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String message = "잘못된 요청입니다.";
        ErrorResponse errorResponse = ErrorResponse.of(statusCode, message);

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            log.error("{} - {} : {}", e.getClass().getSimpleName(), fieldError.getField(), fieldError.getDefaultMessage());
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errorResponse;
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(final ApplicationException e) {
        log.error("{} - {}", e.getClass().getSimpleName(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(e.getStatusCode(), e.getMessage());
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }
}
