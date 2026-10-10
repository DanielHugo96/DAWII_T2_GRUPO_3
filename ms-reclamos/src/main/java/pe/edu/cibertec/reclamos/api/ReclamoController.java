package pe.edu.cibertec.reclamos.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.reclamos.service.ReclamoService;

import java.net.URI;

@RestController
@RequestMapping("/api/reclamos")
public class ReclamoController {

    private final ReclamoService service;

    public ReclamoController(ReclamoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReclamoResponse> registrar(@Valid @RequestBody ReclamoCreateRequest request) {
        ReclamoResponse creado = service.registrar(request);
        return ResponseEntity.created(URI.create("/api/reclamos/" + creado.numreclamo())).body(creado);
    }

    @GetMapping("/{numreclamo}")
    public ReclamoResponse consultar(@PathVariable Long numreclamo) {
        return service.consultar(numreclamo);
    }

    @PutMapping("/{numreclamo}")
    public ReclamoResponse actualizarResolucion(
            @PathVariable Long numreclamo,
            @Valid @RequestBody ReclamoResolutionRequest request
    ) {
        return service.actualizarResolucion(numreclamo, request);
    }
}
