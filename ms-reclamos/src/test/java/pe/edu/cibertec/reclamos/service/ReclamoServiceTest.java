package pe.edu.cibertec.reclamos.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.reclamos.api.ReclamoCreateRequest;
import pe.edu.cibertec.reclamos.api.ReclamoResolutionRequest;
import pe.edu.cibertec.reclamos.api.ReclamoResponse;
import pe.edu.cibertec.reclamos.domain.Reclamo;
import pe.edu.cibertec.reclamos.domain.ReclamoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReclamoServiceTest {

    @Mock
    private ReclamoRepository repository;

    @InjectMocks
    private ReclamoService service;

    @Test
    void registrarAplicaLosValoresInicialesSolicitados() {
        when(repository.save(any(Reclamo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReclamoResponse response = service.registrar(
                new ReclamoCreateRequest("Ana Pérez", "1234", "Consumo desconocido"));

        assertNotNull(response.fechareclamo());
        assertEquals("No asignado", response.analista());
        assertEquals(1, response.estado());
        assertEquals("por evaluar", response.observacion());
    }

    @Test
    void actualizarGuardaLaResolucionYElAnalista() {
        Reclamo pending = new Reclamo("Ana Pérez", "1234", "Consumo desconocido");
        when(repository.findById(10L)).thenReturn(Optional.of(pending));
        when(repository.save(any(Reclamo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReclamoResponse response = service.actualizarResolucion(
                10L, new ReclamoResolutionRequest("María Riesgos", 2, "Consumo no reconocido"));

        assertEquals(2, response.estado());
        assertEquals("María Riesgos", response.analista());
        assertEquals("Consumo no reconocido", response.observacion());
    }

    @Test
    void noPermiteActualizarUnReclamoYaResuelto() {
        Reclamo resolved = new Reclamo("Ana Pérez", "1234", "Consumo desconocido");
        resolved.resolver("María Riesgos", 3, "No procede");
        when(repository.findById(10L)).thenReturn(Optional.of(resolved));

        assertThrows(ReclamoNoPendienteException.class, () -> service.actualizarResolucion(
                10L, new ReclamoResolutionRequest("Otro analista", 2, "Nueva evaluación")));
    }

    @Test
    void consultarInformaCuandoElReclamoNoExiste() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ReclamoNoEncontradoException.class, () -> service.consultar(404L));
        verify(repository).findById(404L);
    }
}
