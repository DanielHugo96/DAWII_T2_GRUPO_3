package pe.edu.cibertec.riesgo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.riesgo.api.RiesgoResolutionRequest;
import pe.edu.cibertec.riesgo.api.RiesgoResolutionResponse;
import pe.edu.cibertec.riesgo.client.ReclamoResolutionRequest;
import pe.edu.cibertec.riesgo.client.ReclamoResponse;
import pe.edu.cibertec.riesgo.client.ReclamosClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiesgoServiceTest {

    @Mock
    private ReclamosClient reclamosClient;

    @InjectMocks
    private RiesgoService service;

    @Test
    void validaExistenciaYActualizaPorFeign() {
        when(reclamosClient.consultar(7L)).thenReturn(reclamo(7L, 1));
        when(reclamosClient.actualizarResolucion(7L,
                new ReclamoResolutionRequest("Analista", 2, "Procedente: Consumo validado como desconocido")))
                .thenReturn(reclamo(7L, 2));

        RiesgoResolutionResponse response = service.resolver(7L,
                new RiesgoResolutionRequest("Analista", 2, "Consumo validado como desconocido"));

        assertEquals(2, response.reclamo().estado());
        assertEquals("procedente", response.mensaje().substring(response.mensaje().lastIndexOf(' ') + 1));
        verify(reclamosClient).consultar(7L);
        verify(reclamosClient).actualizarResolucion(7L,
                new ReclamoResolutionRequest("Analista", 2, "Procedente: Consumo validado como desconocido"));
    }

    @Test
    void noActualizaSiElReclamoNoEstaPendiente() {
        when(reclamosClient.consultar(7L)).thenReturn(reclamo(7L, 3));

        assertThrows(ReclamoNoPendienteException.class, () -> service.resolver(7L,
                new RiesgoResolutionRequest("Analista", 2, "Revisión duplicada")));
    }

    private ReclamoResponse reclamo(Long id, Integer estado) {
        return new ReclamoResponse(id, LocalDateTime.now(), "Ana Pérez", "1234",
                "Consumo desconocido", "Analista", estado, "En revisión");
    }
}
