package pe.edu.cibertec.reclamos.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.cibertec.reclamos.service.ReclamoNoEncontradoException;
import pe.edu.cibertec.reclamos.service.ReclamoNoPendienteException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ReclamoNoEncontradoException.class)
    public ResponseEntity<ApiError> noEncontrado(ReclamoNoEncontradoException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ReclamoNoPendienteException.class)
    public ResponseEntity<ApiError> conflicto(ReclamoNoPendienteException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> solicitudInvalida(Exception ex, HttpServletRequest request) {
        String mensaje = ex instanceof MethodArgumentNotValidException validation
                ? validation.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .findFirst().orElse("La solicitud no es válida")
                : "La solicitud no es válida: revise los datos enviados";
        return respuesta(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    private ResponseEntity<ApiError> respuesta(HttpStatus status, String mensaje, HttpServletRequest request) {
        ApiError error = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje,
                request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
