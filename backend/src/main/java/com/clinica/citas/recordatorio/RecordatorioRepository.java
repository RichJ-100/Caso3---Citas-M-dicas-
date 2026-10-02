package com.clinica.citas.recordatorio;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {
    Optional<Recordatorio> findByCitaId(Long citaId);
    List<Recordatorio> findByEstadoAndProximoIntentoLessThanEqual(EstadoRecordatorio estado, LocalDateTime hasta);
}
