package pe.edu.cibertec.reclamos.service;

public class ReclamoNoPendienteException extends RuntimeException {
    public ReclamoNoPendienteException(Long numreclamo) {
        super("El reclamo " + numreclamo + " ya fue resuelto y no puede volver a procesarse");
    }
}
