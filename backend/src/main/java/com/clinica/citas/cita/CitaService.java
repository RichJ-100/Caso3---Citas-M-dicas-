package com.clinica.citas.cita;

import com.clinica.citas.agenda.*;
import com.clinica.citas.auditoria.AuditoriaService;
import com.clinica.citas.paciente.PacienteRepository;
import com.clinica.citas.recordatorio.*;
import com.clinica.citas.seguridad.Actor;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CitaService {
    private final CitaRepository citas; private final SlotRepository slots; private final PacienteRepository pacientes; private final RecordatorioRepository recordatorios; private final AuditoriaService auditoria;
    public CitaService(CitaRepository citas, SlotRepository slots, PacienteRepository pacientes, RecordatorioRepository recordatorios, AuditoriaService auditoria) { this.citas = citas; this.slots = slots; this.pacientes = pacientes; this.recordatorios = recordatorios; this.auditoria = auditoria; }
    @Transactional public Cita reservar(Actor actor, Long pacienteId, Long slotId, String tipoConsulta) {
        actor.exigirRol(Actor.Rol.PACIENTE, Actor.Rol.RECEPCION);
        if (actor.rol() == Actor.Rol.PACIENTE && !pacienteId.equals(actor.id())) throw prohibido("Un paciente solo puede reservar para si mismo");
        pacientes.findById(pacienteId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        Slot slot = slots.buscarConBloqueo(slotId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Horario no encontrado"));
        if (slot.getEstado() != EstadoSlot.LIBRE) { auditoria.registrarAparte(actor.etiqueta(), "RESERVA_RECHAZADA", "SLOT", slotId, "horario ya ocupado, pacienteId=" + pacienteId); throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese horario ya fue reservado"); }
        if (!slot.getInicio().isAfter(LocalDateTime.now())) { auditoria.registrarAparte(actor.etiqueta(), "RESERVA_RECHAZADA", "SLOT", slotId, "horario en el pasado"); throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese horario ya paso"); }
        slot.reservar(); Cita cita = citas.save(new Cita(pacienteId, slot.getProfesionalId(), slot.getId(), slot.getInicio(), tipoConsulta));
        recordatorios.save(new Recordatorio(cita.getId(), LocalDateTime.now()));
        auditoria.registrar(actor.etiqueta(), "CITA_RESERVADA", "CITA", cita.getId(), "slotId=" + slotId);
        return cita;
    }
    @Transactional public Cita cancelar(Actor actor, Long citaId, String motivo) {
        Cita cita = buscar(citaId); verificarAcceso(actor, cita); if (cita.getEstado() == EstadoCita.CANCELADA) return cita;
        slots.buscarConBloqueo(cita.getSlotId()).orElseThrow().liberar(); cita.cancelar(motivo); recordatorios.findByCitaId(citaId).ifPresent(Recordatorio::cancelar); auditoria.registrar(actor.etiqueta(), "CITA_CANCELADA", "CITA", citaId, "motivo=" + motivo); return cita;
    }
    @Transactional(readOnly = true) public Cita ver(Actor actor, Long id) { Cita cita = buscar(id); verificarAcceso(actor, cita); return cita; }
    @Transactional(readOnly = true) public List<Cita> listar(Actor actor) { return switch (actor.rol()) { case PACIENTE -> citas.findByPacienteIdOrderByInicioAsc(actor.id()); case MEDICO -> citas.findByProfesionalIdOrderByInicioAsc(actor.id()); case RECEPCION -> citas.findAll(); default -> throw prohibido("Tu rol no puede listar citas"); }; }
    @Transactional(readOnly = true) public Recordatorio verRecordatorio(Actor actor, Long id) { ver(actor, id); return recordatorios.findByCitaId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sin recordatorio")); }
    private Cita buscar(Long id) { return citas.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada")); }
    private void verificarAcceso(Actor actor, Cita cita) { switch (actor.rol()) { case RECEPCION -> { } case PACIENTE -> { if (!cita.getPacienteId().equals(actor.id())) throw prohibido("Esta cita no es tuya"); } case MEDICO -> { if (!cita.getProfesionalId().equals(actor.id())) throw prohibido("Esta cita no es de tu agenda"); } default -> throw prohibido("Tu rol no puede acceder a las citas"); } }
    private ResponseStatusException prohibido(String mensaje) { return new ResponseStatusException(HttpStatus.FORBIDDEN, mensaje); }
}
