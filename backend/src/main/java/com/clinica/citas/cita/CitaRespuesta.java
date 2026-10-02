package com.clinica.citas.cita;

import java.time.LocalDateTime;

public record CitaRespuesta(Long id, Long pacienteId, Long profesionalId, Long slotId, LocalDateTime inicio, EstadoCita estado, String tipoConsulta) {
    public static CitaRespuesta de(Cita c) { return new CitaRespuesta(c.getId(), c.getPacienteId(), c.getProfesionalId(), c.getSlotId(), c.getInicio(), c.getEstado(), c.getTipoConsulta()); }
}
