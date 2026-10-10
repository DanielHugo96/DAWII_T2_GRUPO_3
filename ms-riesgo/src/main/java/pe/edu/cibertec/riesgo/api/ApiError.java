package pe.edu.cibertec.riesgo.api;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime timestamp, int status, String error, String mensaje, String path) {
}
