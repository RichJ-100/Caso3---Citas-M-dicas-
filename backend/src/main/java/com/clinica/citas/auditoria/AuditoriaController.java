package com.clinica.citas.auditoria;

import com.clinica.citas.seguridad.Actor;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {
    private final AuditoriaService auditoria;
    public AuditoriaController(AuditoriaService auditoria) { this.auditoria = auditoria; }
    @GetMapping
    public List<Auditoria> listar(@RequestHeader("X-Rol") String rol, @RequestHeader(value = "X-Usuario-Id", required = false) Long usuarioId, @RequestParam String entidad, @RequestParam Long entidadId) {
        Actor actor = Actor.de(rol, usuarioId); actor.exigirRol(Actor.Rol.AUDITOR);
        return auditoria.listar(entidad.toUpperCase(), entidadId);
    }
}
