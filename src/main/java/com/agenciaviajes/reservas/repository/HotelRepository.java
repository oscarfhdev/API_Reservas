package com.agenciaviajes.reservas.repository;

import com.agenciaviajes.reservas.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByDisponibilidadTrue(); // Así encuentra los disponibles "true"
}
