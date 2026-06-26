package deu_branco_api.controller;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import deu_branco_api.controller.dto.ErroResponse;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> tratarIllegalArgumentException(IllegalArgumentException exception) {
        return erro(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String mensagem = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .orElse("Dados invalidos.");

        return erro(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponse> tratarEntityNotFoundException(EntityNotFoundException exception) {
        return erro(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponse> tratarAccessDeniedException(AccessDeniedException exception) {
        return erro(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    private ResponseEntity<ErroResponse> erro(HttpStatus status, String mensagem) {
        ErroResponse response = new ErroResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem);

        return ResponseEntity.status(status).body(response);
    }
}
