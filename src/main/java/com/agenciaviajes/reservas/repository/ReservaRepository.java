package com.agenciaviajes.reservas.repository;

import com.agenciaviajes.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
