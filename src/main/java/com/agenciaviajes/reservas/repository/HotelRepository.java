package com.agenciaviajes.reservas.repository;

import com.agenciaviajes.reservas.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
