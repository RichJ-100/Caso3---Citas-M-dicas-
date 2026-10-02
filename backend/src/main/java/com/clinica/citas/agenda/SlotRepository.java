package com.clinica.citas.agenda;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByProfesionalIdAndEstadoAndInicioBetweenOrderByInicioAsc(Long profesionalId, EstadoSlot estado, LocalDateTime desde, LocalDateTime hasta);
    boolean existsByProfesionalIdAndInicio(Long profesionalId, LocalDateTime inicio);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s where s.id = :id")
    Optional<Slot> buscarConBloqueo(@Param("id") Long id);
}
