package fr.avahip.apifcchamptracker.handler;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static fr.avahip.apifcchamptracker.handler.ErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exception) {
        return ResponseEntity.status(UNAUTHORIZED).body(ExceptionResponse.builder()
                .code(ACCOUNT_LOCKED.getCode())
                .exceptionDescription(ACCOUNT_LOCKED.getDescription())
                .error(exception.getMessage())
                .build()
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exception) {
        return ResponseEntity.status(UNAUTHORIZED).body(ExceptionResponse.builder()
                        .code(ACCOUNT_DISABLED.getCode())
                        .exceptionDescription(ACCOUNT_DISABLED.getDescription())
                        .error(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exception) {
        return ResponseEntity.status(UNAUTHORIZED).body(ExceptionResponse.builder()
                        .code(BAD_CREDENTIALS.getCode())
                        .exceptionDescription(BAD_CREDENTIALS.getDescription())
                        .error(BAD_CREDENTIALS.getDescription())
                        .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ExceptionResponse.builder()
                        .error(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        return ResponseEntity.status(BAD_REQUEST).body(ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ExceptionResponse.builder()
                        .exceptionDescription("Internal error, please contact the administrator")
                        .error(exception.getMessage())
                        .build()
                );
    }
}
