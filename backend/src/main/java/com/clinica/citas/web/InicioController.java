package com.clinica.citas.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class InicioController {
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String inicio() { return "<h1>Citas medicas - Clinica</h1><ul><li><a href=\"/api/health\">Estado del servicio</a></li><li><a href=\"/api/profesionales\">Profesionales</a></li><li><a href=\"/swagger-ui/index.html\">Swagger UI</a></li></ul>"; }
}
