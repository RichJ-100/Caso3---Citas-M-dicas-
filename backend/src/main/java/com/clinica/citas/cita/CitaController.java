package com.clinica.citas.cita;

import com.clinica.citas.recordatorio.Recordatorio;
import com.clinica.citas.seguridad.Actor;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
public class CitaController {
    private final CitaService service;
    public CitaController(CitaService service) { this.service = service; }
    @PostMapping public ResponseEntity<CitaRespuesta> reservar(@RequestHeader("X-Rol") String rol, @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId, @Valid @RequestBody SolicitudCita solicitud) { Cita c = service.reservar(Actor.de(rol, usuarioId), solicitud.pacienteId(), solicitud.slotId(), solicitud.tipoConsulta()); return ResponseEntity.status(HttpStatus.CREATED).body(CitaRespuesta.de(c)); }
    @GetMapping public List<CitaRespuesta> listar(@RequestHeader("X-Rol") String rol, @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId) { return service.listar(Actor.de(rol, usuarioId)).stream().map(CitaRespuesta::de).toList(); }
    @GetMapping("/{id}") public CitaRespuesta ver(@RequestHeader("X-Rol") String rol, @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId, @PathVariable Long id) { return CitaRespuesta.de(service.ver(Actor.de(rol, usuarioId), id)); }
    @PostMapping("/{id}/cancelar") public CitaRespuesta cancelar(@RequestHeader("X-Rol") String rol, @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId, @PathVariable Long id, @RequestParam(required = false) String motivo) { return CitaRespuesta.de(service.cancelar(Actor.de(rol, usuarioId), id, motivo)); }
    @GetMapping("/{id}/recordatorio") public Recordatorio recordatorio(@RequestHeader("X-Rol") String rol, @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId, @PathVariable Long id) { return service.verRecordatorio(Actor.de(rol, usuarioId), id); }
}
