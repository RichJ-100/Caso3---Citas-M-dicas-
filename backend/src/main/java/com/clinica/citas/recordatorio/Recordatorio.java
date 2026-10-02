package com.clinica.citas.recordatorio;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recordatorio")
public class Recordatorio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "cita_id", nullable = false) private Long citaId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private EstadoRecordatorio estado;
    @Column(nullable = false) private int intentos;
    @Column(nullable = false) private LocalDateTime proximoIntento;
    @Column(length = 300) private String ultimoError;
    protected Recordatorio() { }
    public Recordatorio(Long citaId, LocalDateTime enviarEn) { this.citaId = citaId; estado = EstadoRecordatorio.PENDIENTE; proximoIntento = enviarEn; }
    public void marcarEnviado() { intentos++; estado = EstadoRecordatorio.ENVIADO; ultimoError = null; }
    public void registrarFallo(String error, int maxIntentos) { intentos++; ultimoError = error; if (intentos >= maxIntentos) estado = EstadoRecordatorio.FALLIDO; else proximoIntento = LocalDateTime.now().plusSeconds(30L * intentos); }
    public void cancelar() { if (estado == EstadoRecordatorio.PENDIENTE) estado = EstadoRecordatorio.CANCELADO; }
    public Long getId() { return id; } public Long getCitaId() { return citaId; } public EstadoRecordatorio getEstado() { return estado; } public int getIntentos() { return intentos; } public LocalDateTime getProximoIntento() { return proximoIntento; } public String getUltimoError() { return ultimoError; }
}
