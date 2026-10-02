package com.clinica.citas.auditoria;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

@Service
public class AuditoriaService {
    private final AuditoriaRepository repositorio;
    public AuditoriaService(AuditoriaRepository repositorio) { this.repositorio = repositorio; }
    @Transactional public void registrar(String actor, String accion, String entidad, Long entidadId, String detalle) { repositorio.save(new Auditoria(actor, accion, entidad, entidadId, detalle)); }
    @Transactional(propagation = Propagation.REQUIRES_NEW) public void registrarAparte(String actor, String accion, String entidad, Long entidadId, String detalle) { repositorio.save(new Auditoria(actor, accion, entidad, entidadId, detalle)); }
    public List<Auditoria> listar(String entidad, Long entidadId) { return repositorio.findByEntidadAndEntidadIdOrderByIdAsc(entidad, entidadId); }
}
