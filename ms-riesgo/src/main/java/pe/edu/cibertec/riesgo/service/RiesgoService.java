package pe.edu.cibertec.riesgo.service;

import org.springframework.stereotype.Service;
import pe.edu.cibertec.riesgo.api.RiesgoResolutionRequest;
import pe.edu.cibertec.riesgo.api.RiesgoResolutionResponse;
import pe.edu.cibertec.riesgo.client.ReclamoResolutionRequest;
import pe.edu.cibertec.riesgo.client.ReclamoResponse;
import pe.edu.cibertec.riesgo.client.ReclamosClient;

@Service
public class RiesgoService {

    private final ReclamosClient reclamosClient;

    public RiesgoService(ReclamosClient reclamosClient) {
        this.reclamosClient = reclamosClient;
    }

    public RiesgoResolutionResponse resolver(Long numreclamo, RiesgoResolutionRequest request) {
        ReclamoResponse actual = reclamosClient.consultar(numreclamo);
        if (actual.estado() == null || actual.estado() != 1) {
            throw new ReclamoNoPendienteException(numreclamo);
        }

        String calificacion = request.estado() == 2 ? "Procedente" : "Improcedente";
        String observacion = calificacion + ": " + request.observacion().trim();
        ReclamoResponse actualizado = reclamosClient.actualizarResolucion(
                numreclamo,
                new ReclamoResolutionRequest(request.analista().trim(), request.estado(), observacion)
        );
        String resultado = actualizado.estado() == 2 ? "procedente" : "improcedente";
        return new RiesgoResolutionResponse(
                "Reclamo " + numreclamo + " resuelto correctamente como " + resultado,
                actualizado
        );
    }
}
