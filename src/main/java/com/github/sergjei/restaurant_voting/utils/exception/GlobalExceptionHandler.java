package com.github.sergjei.restaurant_voting.utils.exception;

import jakarta.persistence.EntityNotFoundException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.github.sergjei.restaurant_voting.utils.ValidationUtil;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private final ErrorAttributes errorAttributes;

    public GlobalExceptionHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(WebRequest request, AppException ex) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        return createResponseEntity(request, ex.getOptions(), null, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(WebRequest request, EntityNotFoundException ex) {
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleJdbcSQLIntegrityConstraintViolationException(WebRequest request,
                                                                                JdbcSQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        String start = "Unique index or primary key violation:";
        if (message.toLowerCase().contains("restaurant_uniq_menuitem_date")) {
            message = start + " restaurant already has menuitem with same description at this date!";
        } else if (message.toLowerCase().toLowerCase().contains("restaurant_name_address")) {
            message = start + " restaurant with same email and name already exist";
        } else if (message.toLowerCase().contains("user_email")) {
            message = start + " user with same email already exist!";
        } else if (message.toLowerCase().contains("user_vote_per_day")) {
            message = start + " user has already voted today!";
        }else if (message.toLowerCase().contains("restaurant_menu_item")) {
            message = start + " menu item with this id already belongs to another restaurant!";
        }

        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(WebRequest request, IllegalArgumentException ex) {
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(WebRequest request, IllegalStateException ex) {
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Throwable t = ValidationUtil.getRootCause(ex);
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable t = ValidationUtil.getRootCause(ex);
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        Throwable t = ValidationUtil.getRootCause(ex);
        return createResponseEntity(request, ErrorAttributeOptions.of(MESSAGE), t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBindException(BindException ex, WebRequest request) {
        Throwable t = ValidationUtil.getRootCause(ex);
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String msg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        return createResponseEntity(request, ErrorAttributeOptions.defaults(), msg, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @SuppressWarnings("unchecked")
    private <T> ResponseEntity<T> createResponseEntity(WebRequest request, ErrorAttributeOptions options, String msg, HttpStatus status) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg != null) {
            body.put("message", msg);
        }
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }

}
