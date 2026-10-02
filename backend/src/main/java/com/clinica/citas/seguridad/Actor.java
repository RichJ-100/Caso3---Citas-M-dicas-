package com.clinica.citas.seguridad;

import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public record Actor(Rol rol, Long id) {
    public enum Rol { PACIENTE, RECEPCION, MEDICO, AUDITOR }

    public static Actor de(String rol, Long id) {
        Rol r;
        try {
            r = Rol.valueOf(rol.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Rol invalido. Usa PACIENTE, RECEPCION, MEDICO o AUDITOR");
        }
        if ((r == Rol.PACIENTE || r == Rol.MEDICO) && id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Falta el header X-Usuario-Id para este rol");
        }
        return new Actor(r, id);
    }

    public String etiqueta() { return id == null ? rol.name() : rol.name() + ":" + id; }

    public void exigirRol(Rol... permitidos) {
        if (!Arrays.asList(permitidos).contains(rol)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tu rol no puede hacer esto");
        }
    }
}
