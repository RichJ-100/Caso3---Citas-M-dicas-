package com.clinica.citas.cita;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SolicitudCita(@NotNull Long pacienteId, @NotNull Long slotId, @Size(max = 100) String tipoConsulta) { }
