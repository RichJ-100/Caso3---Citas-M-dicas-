package com.clinica.citas.agenda;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "slot", uniqueConstraints = @UniqueConstraint(columnNames = {"profesional_id", "inicio"}))
public class Slot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "profesional_id", nullable = false) private Long profesionalId;
    @Column(nullable = false) private LocalDateTime inicio;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoSlot estado;
    protected Slot() { }
    public Slot(Long profesionalId, LocalDateTime inicio) { this.profesionalId = profesionalId; this.inicio = inicio; this.estado = EstadoSlot.LIBRE; }
    public void reservar() { estado = EstadoSlot.RESERVADO; }
    public void liberar() { estado = EstadoSlot.LIBRE; }
    public Long getId() { return id; }
    public Long getProfesionalId() { return profesionalId; }
    public LocalDateTime getInicio() { return inicio; }
    public EstadoSlot getEstado() { return estado; }
}
