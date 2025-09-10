package sideprojects.dreamdecoder.global.shared.exception;

import static sideprojects.dreamdecoder.global.shared.response.CommonErrorCode.*;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

import java.util.NoSuchElementException;
import sideprojects.dreamdecoder.global.shared.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableExceptionException(HttpMessageNotReadableException ex) {
        ErrorCode errorCode = INVALID_INPUT_VALUE;
        log.error("HttpMessageNotReadableException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorCode errorCode = INVALID_INPUT_VALUE;
        log.error("DataIntegrityViolationException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        ErrorCode errorCode = NOT_FOUND;
        log.error("NoSuchElementException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorCode errorCode = INVALID_TYPE_VALUE;
        log.error("MethodArgumentTypeMismatchException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(ex));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorCode errorCode = METHOD_NOT_ALLOWED;
        log.error("HttpRequestMethodNotSupportedException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ErrorCode errorCode = METHOD_NOT_ALLOWED;
        log.error("MissingServletRequestParameterException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, ex.getParameterName()));
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        ErrorCode errorCode = INVALID_INPUT_VALUE;
        log.error("BindException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, ex.getBindingResult()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorCode errorCode = INVALID_INPUT_VALUE;
        log.error("ConstraintViolationException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, ex.getConstraintViolations()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = INVALID_INPUT_VALUE;
        log.error("MethodArgumentNotValidException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, ex.getBindingResult()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorCode errorCode = INVALID_INPUT_VALUE;
        log.error("HttpMessageNotReadableException 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("CustomException 발생: {}", ex.getMessage(), ex);
        ErrorResponse body = ex.getErrors().isEmpty()
            ? ErrorResponse.of(errorCode)
            : ErrorResponse.of(errorCode, ex.getErrors());
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

}