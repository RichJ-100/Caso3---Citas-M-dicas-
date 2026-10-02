package com.clinica.citas.integracion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Component
public class CorreoSimulado implements ProveedorCorreo {
    private static final Logger log = LoggerFactory.getLogger(CorreoSimulado.class);
    private final Map<String, Integer> intentos = new ConcurrentHashMap<>();
    public void enviar(String destino, String asunto, String cuerpo) {
        if (destino.startsWith("falla")) throw new EnvioFallidoException("Proveedor de correo caido (simulado)");
        if (destino.startsWith("reintento") && intentos.merge(destino, 1, Integer::sum) <= 2) throw new EnvioFallidoException("Fallo temporal del proveedor (simulado)");
        log.info("CORREO ENVIADO a {} | {} | {}", destino, asunto, cuerpo);
    }
}
