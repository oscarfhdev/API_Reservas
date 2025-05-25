package com.agenciaviajes.reservas.repository;

import com.agenciaviajes.reservas.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
}
