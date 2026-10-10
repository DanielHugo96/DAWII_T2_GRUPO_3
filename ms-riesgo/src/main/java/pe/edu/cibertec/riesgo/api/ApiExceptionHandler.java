package pe.edu.cibertec.riesgo.api;

import feign.FeignException;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.cibertec.riesgo.service.ReclamoNoPendienteException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ApiError> reclamoNoEncontrado(FeignException.NotFound ex, HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, "No existe el reclamo solicitado", request);
    }

    @ExceptionHandler(ReclamoNoPendienteException.class)
    public ResponseEntity<ApiError> reclamoYaResuelto(ReclamoNoPendienteException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ApiError> servicioNoDisponible(RetryableException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE,
                "No se pudo contactar al servicio de reclamos; confirme que esté registrado en Eureka", request);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> errorFeign(FeignException ex, HttpServletRequest request) {
        HttpStatus status = ex.status() == 400 ? HttpStatus.BAD_REQUEST
                : ex.status() == 409 ? HttpStatus.CONFLICT : HttpStatus.BAD_GATEWAY;
        return respuesta(status, "El servicio de reclamos rechazó o no pudo procesar la solicitud", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> solicitudInvalida(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst().orElse("La solicitud no es válida");
        return respuesta(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    private ResponseEntity<ApiError> respuesta(HttpStatus status, String mensaje, HttpServletRequest request) {
        ApiError error = new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje,
                request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
