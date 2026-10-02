package com.clinica.citas.agenda;

import java.time.*;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AgendaController {
    private final ProfesionalRepository profesionales;
    private final SlotRepository slots;
    public AgendaController(ProfesionalRepository profesionales, SlotRepository slots) { this.profesionales = profesionales; this.slots = slots; }
    @GetMapping("/profesionales") public List<Profesional> profesionales() { return profesionales.findAll(); }
    @GetMapping("/disponibilidad")
    public List<Slot> disponibilidad(@RequestParam Long profesionalId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDateTime desde = fecha.atStartOfDay(), hasta = fecha.plusDays(1).atStartOfDay();
        if (desde.isBefore(LocalDateTime.now())) desde = LocalDateTime.now();
        return slots.findByProfesionalIdAndEstadoAndInicioBetweenOrderByInicioAsc(profesionalId, EstadoSlot.LIBRE, desde, hasta);
    }
}
