package pe.edu.cibertec.riesgo.service;

public class ReclamoNoPendienteException extends RuntimeException {
    public ReclamoNoPendienteException(Long numreclamo) {
        super("El reclamo " + numreclamo + " no está pendiente y no puede resolverse nuevamente");
    }
}
