package pe.edu.cibertec.riesgo.api;

import pe.edu.cibertec.riesgo.client.ReclamoResponse;

public record RiesgoResolutionResponse(String mensaje, ReclamoResponse reclamo) {
}
