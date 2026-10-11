package pe.edu.cibertec.riesgo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Cliente OpenFeign para la comunicación con ms-reclamos

@FeignClient(name = "ms-reclamos", path = "/api/reclamos")
public interface ReclamosClient {

    @GetMapping("/{numreclamo}")
    ReclamoResponse consultar(@PathVariable("numreclamo") Long numreclamo);

    @PutMapping("/{numreclamo}")
    ReclamoResponse actualizarResolucion(
            @PathVariable("numreclamo") Long numreclamo,
            @RequestBody ReclamoResolutionRequest request
    );
    // Revisión y validación de cliente Feign
}
