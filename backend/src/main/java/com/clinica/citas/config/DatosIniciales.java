package com.clinica.citas.config;

import com.clinica.citas.agenda.*;
import com.clinica.citas.paciente.*;
import java.time.*;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {
    private final PacienteRepository pacientes; private final ProfesionalRepository profesionales; private final SlotRepository slots;
    public DatosIniciales(PacienteRepository pacientes, ProfesionalRepository profesionales, SlotRepository slots) { this.pacientes = pacientes; this.profesionales = profesionales; this.slots = slots; }
    public void run(String... args) {
        if (pacientes.count() == 0) { pacientes.save(new Paciente("Ana Perez", "1700000001", "ana@correo.com", "0990000001")); pacientes.save(new Paciente("Luis Mora", "1700000002", "reintento@correo.com", "0990000002")); pacientes.save(new Paciente("Eva Soto", "1700000003", "falla@correo.com", "0990000003")); }
        if (profesionales.count() == 0) { profesionales.save(new Profesional("Dra. Carla Vega", "Medicina general")); profesionales.save(new Profesional("Dr. Mario Ruiz", "Pediatria")); }
        if (slots.count() == 0) { LocalDateTime ahora = LocalDateTime.now(); List<Profesional> lista = profesionales.findAll(); for (Profesional p : lista) for (int dia = 0; dia <= 3; dia++) for (int hora = 9; hora <= 11; hora++) for (int minuto : new int[] {0, 30}) { LocalDateTime inicio = LocalDate.now().plusDays(dia).atTime(hora, minuto); if (inicio.isAfter(ahora)) slots.save(new Slot(p.getId(), inicio)); } }
    }
}
