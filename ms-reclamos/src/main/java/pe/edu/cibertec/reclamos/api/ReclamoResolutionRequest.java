package pe.edu.cibertec.reclamos.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReclamoResolutionRequest(
        @NotBlank @Size(max = 120) String analista,
        @NotNull @Min(2) @Max(3) Integer estado,
        @NotBlank @Size(max = 1000) String observacion
) {
}
