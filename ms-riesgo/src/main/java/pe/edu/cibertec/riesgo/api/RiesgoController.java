package pe.edu.cibertec.riesgo.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.riesgo.service.RiesgoService;

@RestController
@RequestMapping("/api/riesgo/reclamos")
public class RiesgoController {

    private final RiesgoService service;

    public RiesgoController(RiesgoService service) {
        this.service = service;
    }

    @PutMapping("/{numreclamo}/resolver")
    public ResponseEntity<RiesgoResolutionResponse> resolver(
            @PathVariable Long numreclamo,
            @Valid @RequestBody RiesgoResolutionRequest request
    ) {
        return ResponseEntity.ok(service.resolver(numreclamo, request));
    }
}
