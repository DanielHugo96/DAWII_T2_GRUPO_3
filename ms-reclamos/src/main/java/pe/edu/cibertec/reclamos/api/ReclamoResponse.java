package pe.edu.cibertec.reclamos.api;

import pe.edu.cibertec.reclamos.domain.Reclamo;

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
    public static ReclamoResponse desde(Reclamo reclamo) {
        return new ReclamoResponse(
                reclamo.getNumreclamo(),
                reclamo.getFechareclamo(),
                reclamo.getNomcliente(),
                reclamo.getUltimos4tarjeta(),
                reclamo.getMotivo(),
                reclamo.getAnalista(),
                reclamo.getEstado(),
                reclamo.getObservacion()
        );
    }
}
