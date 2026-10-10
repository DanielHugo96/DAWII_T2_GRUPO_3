package pe.edu.cibertec.riesgo.client;

import java.time.LocalDateTime;

public record ReclamoResponse(
        Long numreclamo,
        LocalDateTime fechareclamo,
        String nomcliente,
        String ultimos4tarjeta,
        String motivo,
        String analista,
        Integer estado,
        String observacion
) {
}
