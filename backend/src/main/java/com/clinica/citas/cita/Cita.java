package com.clinica.citas.cita;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
public class Cita {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "paciente_id", nullable = false) private Long pacienteId;
    @Column(name = "profesional_id", nullable = false) private Long profesionalId;
    @Column(name = "slot_id", nullable = false) private Long slotId;
    @Column(nullable = false) private LocalDateTime inicio;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoCita estado;
    @Column(length = 100) private String tipoConsulta;
    @Column(nullable = false) private LocalDateTime creadaEn;
    private LocalDateTime canceladaEn;
    @Column(length = 200) private String motivoCancelacion;
    protected Cita() { }
    public Cita(Long pacienteId, Long profesionalId, Long slotId, LocalDateTime inicio, String tipoConsulta) { this.pacienteId = pacienteId; this.profesionalId = profesionalId; this.slotId = slotId; this.inicio = inicio; this.tipoConsulta = tipoConsulta; this.estado = EstadoCita.CONFIRMADA; this.creadaEn = LocalDateTime.now(); }
    public void cancelar(String motivo) { estado = EstadoCita.CANCELADA; canceladaEn = LocalDateTime.now(); motivoCancelacion = motivo; }
    public Long getId() { return id; } public Long getPacienteId() { return pacienteId; } public Long getProfesionalId() { return profesionalId; } public Long getSlotId() { return slotId; } public LocalDateTime getInicio() { return inicio; } public EstadoCita getEstado() { return estado; } public String getTipoConsulta() { return tipoConsulta; } public LocalDateTime getCreadaEn() { return creadaEn; } public LocalDateTime getCanceladaEn() { return canceladaEn; } public String getMotivoCancelacion() { return motivoCancelacion; }
}
