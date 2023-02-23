package hello.restAPI.web.exceptionManager;

import hello.restAPI.customException.CustomUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<?> userExceptionHandler(CustomUserException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().name() + "" + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
