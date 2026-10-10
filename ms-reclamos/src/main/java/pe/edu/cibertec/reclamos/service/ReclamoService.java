package pe.edu.cibertec.reclamos.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.reclamos.api.ReclamoCreateRequest;
import pe.edu.cibertec.reclamos.api.ReclamoResolutionRequest;
import pe.edu.cibertec.reclamos.api.ReclamoResponse;
import pe.edu.cibertec.reclamos.domain.Reclamo;
import pe.edu.cibertec.reclamos.domain.ReclamoRepository;

@Service
public class ReclamoService {

    private final ReclamoRepository repository;

    public ReclamoService(ReclamoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ReclamoResponse registrar(ReclamoCreateRequest request) {
        Reclamo reclamo = new Reclamo(request.nomcliente(), request.ultimos4tarjeta(), request.motivo());
        return ReclamoResponse.desde(repository.save(reclamo));
    }

    @Transactional(readOnly = true)
    public ReclamoResponse consultar(Long numreclamo) {
        return ReclamoResponse.desde(buscar(numreclamo));
    }

    @Transactional
    public ReclamoResponse actualizarResolucion(Long numreclamo, ReclamoResolutionRequest request) {
        Reclamo reclamo = buscar(numreclamo);
        if (reclamo.getEstado() != 1) {
            throw new ReclamoNoPendienteException(numreclamo);
        }
        if (request.estado() != 2 && request.estado() != 3) {
            throw new IllegalArgumentException("El estado final debe ser 2 (procedente) o 3 (improcedente)");
        }
        reclamo.resolver(request.analista().trim(), request.estado(), request.observacion().trim());
        return ReclamoResponse.desde(repository.save(reclamo));
    }

    private Reclamo buscar(Long numreclamo) {
        return repository.findById(numreclamo)
                .orElseThrow(() -> new ReclamoNoEncontradoException(numreclamo));
    }
}
