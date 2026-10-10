package pe.edu.cibertec.reclamos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reclamos")
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numreclamo;

    @Column(nullable = false)
    private LocalDateTime fechareclamo;

    @Column(nullable = false, length = 120)
    private String nomcliente;

    @Column(nullable = false, length = 4)
    private String ultimos4tarjeta;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(nullable = false, length = 120)
    private String analista;

    @Column(nullable = false)
    private Integer estado;

    @Column(nullable = false, length = 1000)
    private String observacion;

    protected Reclamo() {
    }

    public Reclamo(String nomcliente, String ultimos4tarjeta, String motivo) {
        this.fechareclamo = LocalDateTime.now();
        this.nomcliente = nomcliente;
        this.ultimos4tarjeta = ultimos4tarjeta;
        this.motivo = motivo;
        this.analista = "No asignado";
        this.estado = 1;
        this.observacion = "por evaluar";
    }

    @PrePersist
    void aplicarValoresPorDefecto() {
        if (fechareclamo == null) {
            fechareclamo = LocalDateTime.now();
        }
        if (analista == null || analista.isBlank()) {
            analista = "No asignado";
        }
        if (estado == null) {
            estado = 1;
        }
        if (observacion == null || observacion.isBlank()) {
            observacion = "por evaluar";
        }
    }

    public void resolver(String analista, int estado, String observacion) {
        this.analista = analista;
        this.estado = estado;
        this.observacion = observacion;
    }

    public Long getNumreclamo() { return numreclamo; }
    public LocalDateTime getFechareclamo() { return fechareclamo; }
    public String getNomcliente() { return nomcliente; }
    public String getUltimos4tarjeta() { return ultimos4tarjeta; }
    public String getMotivo() { return motivo; }
    public String getAnalista() { return analista; }
    public Integer getEstado() { return estado; }
    public String getObservacion() { return observacion; }
}
