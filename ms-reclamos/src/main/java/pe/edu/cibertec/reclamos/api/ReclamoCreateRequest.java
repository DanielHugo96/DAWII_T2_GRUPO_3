package pe.edu.cibertec.reclamos.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReclamoCreateRequest(
        @NotBlank @Size(max = 120) String nomcliente,
        @NotBlank @Pattern(regexp = "[0-9]{4}", message = "debe contener exactamente 4 dígitos") String ultimos4tarjeta,
        @NotBlank @Size(max = 500) String motivo
) {
}
