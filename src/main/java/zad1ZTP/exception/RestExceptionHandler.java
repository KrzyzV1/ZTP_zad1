package zad1ZTP.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> ApiError.FieldError.builder().field(e.getField()).message(e.getDefaultMessage()).build())
                .collect(Collectors.toList());
        var api = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation failed")
                .errors(errors)
                .build();
        return ResponseEntity.badRequest().body(api);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleBusiness(ValidationException ex) {
        ApiError.FieldError fe = ApiError.FieldError.builder()
                .field(ex.getField())
                .message(ex.getMessage())
                .build();

        var api = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Business validation failed")
                .errors(java.util.List.of(fe))
                .build();
        return ResponseEntity.badRequest().body(api);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        var api = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not found")
                .errors(java.util.List.of(new ApiError.FieldError(null, ex.getMessage())))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(api);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConflict(DataIntegrityViolationException ex) {
        var api = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Data integrity violation")
                .errors(java.util.List.of(new ApiError.FieldError(null, ex.getMostSpecificCause().getMessage())))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(api);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex) {
        var api = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal error")
                .errors(java.util.List.of(new ApiError.FieldError(null, ex.getMessage())))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(api);
    }
}
