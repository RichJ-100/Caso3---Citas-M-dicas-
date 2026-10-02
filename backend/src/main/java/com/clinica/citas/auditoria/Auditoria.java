package com.clinica.citas.auditoria;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String actor;
    @Column(nullable = false) private String accion;
    @Column(nullable = false) private String entidad;
    private Long entidadId;
    @Column(length = 500) private String detalle;
    @Column(nullable = false) private LocalDateTime creadoEn;
    protected Auditoria() { }
    public Auditoria(String actor, String accion, String entidad, Long entidadId, String detalle) { this.actor = actor; this.accion = accion; this.entidad = entidad; this.entidadId = entidadId; this.detalle = detalle; this.creadoEn = LocalDateTime.now(); }
    public Long getId() { return id; } public String getActor() { return actor; } public String getAccion() { return accion; } public String getEntidad() { return entidad; } public Long getEntidadId() { return entidadId; } public String getDetalle() { return detalle; } public LocalDateTime getCreadoEn() { return creadoEn; }
}
