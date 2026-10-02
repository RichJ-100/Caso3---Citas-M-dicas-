package com.clinica.citas.recordatorio;

import com.clinica.citas.auditoria.AuditoriaService;
import com.clinica.citas.cita.*;
import com.clinica.citas.integracion.*;
import com.clinica.citas.paciente.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RecordatorioService {
    private static final int MAX_INTENTOS = 5;
    private final RecordatorioRepository recordatorios; private final CitaRepository citas; private final PacienteRepository pacientes; private final ProveedorCorreo correo; private final AuditoriaService auditoria;
    public RecordatorioService(RecordatorioRepository recordatorios, CitaRepository citas, PacienteRepository pacientes, ProveedorCorreo correo, AuditoriaService auditoria) { this.recordatorios = recordatorios; this.citas = citas; this.pacientes = pacientes; this.correo = correo; this.auditoria = auditoria; }
    @Scheduled(fixedDelay = 15000, initialDelay = 15000) public void procesarProgramado() { procesar(); }
    public int procesar() { List<Recordatorio> pendientes = recordatorios.findByEstadoAndProximoIntentoLessThanEqual(EstadoRecordatorio.PENDIENTE, LocalDateTime.now()); pendientes.forEach(this::enviar); return pendientes.size(); }
    private void enviar(Recordatorio r) {
        Cita cita = citas.findById(r.getCitaId()).orElse(null); Paciente paciente = cita == null ? null : pacientes.findById(cita.getPacienteId()).orElse(null);
        if (cita == null || paciente == null || cita.getEstado() != EstadoCita.CONFIRMADA) { r.cancelar(); recordatorios.save(r); return; }
        try { correo.enviar(paciente.getEmail(), "Recordatorio de cita", "Tienes una cita el " + cita.getInicio()); r.marcarEnviado(); recordatorios.save(r); auditoria.registrar("SISTEMA", "RECORDATORIO_ENVIADO", "CITA", cita.getId(), "intentos=" + r.getIntentos()); }
        catch (EnvioFallidoException e) { r.registrarFallo(e.getMessage(), MAX_INTENTOS); recordatorios.save(r); auditoria.registrar("SISTEMA", r.getEstado() == EstadoRecordatorio.FALLIDO ? "RECORDATORIO_FALLIDO" : "RECORDATORIO_REINTENTO", "CITA", cita.getId(), "intentos=" + r.getIntentos() + " error=" + e.getMessage()); }
    }
}
