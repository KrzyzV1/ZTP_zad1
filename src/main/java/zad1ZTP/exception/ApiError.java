package zad1ZTP.exception;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<FieldError> errors;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
