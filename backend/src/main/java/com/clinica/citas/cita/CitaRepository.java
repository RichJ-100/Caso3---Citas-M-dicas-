package com.clinica.citas.cita;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteIdOrderByInicioAsc(Long pacienteId);
    List<Cita> findByProfesionalIdOrderByInicioAsc(Long profesionalId);
}
