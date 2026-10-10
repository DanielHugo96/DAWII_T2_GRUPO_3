package pe.edu.cibertec.reclamos.service;

public class ReclamoNoEncontradoException extends RuntimeException {
    public ReclamoNoEncontradoException(Long numreclamo) {
        super("No existe el reclamo " + numreclamo);
    }
}
