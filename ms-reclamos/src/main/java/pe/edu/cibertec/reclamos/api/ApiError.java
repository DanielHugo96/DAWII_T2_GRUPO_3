package pe.edu.cibertec.reclamos.api;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime timestamp, int status, String error, String mensaje, String path) {
}
