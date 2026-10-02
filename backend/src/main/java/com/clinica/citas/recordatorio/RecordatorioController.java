package com.clinica.citas.recordatorio;

import com.clinica.citas.seguridad.Actor;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recordatorios")
public class RecordatorioController {
    private final RecordatorioService service;
    public RecordatorioController(RecordatorioService service) { this.service = service; }
    @PostMapping("/procesar")
    public Map<String, Integer> procesar(@RequestHeader("X-Rol") String rol, @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId) { Actor.de(rol, usuarioId).exigirRol(Actor.Rol.RECEPCION); return Map.of("procesados", service.procesar()); }
}
