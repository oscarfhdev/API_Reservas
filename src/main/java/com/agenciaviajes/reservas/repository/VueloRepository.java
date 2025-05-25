package com.agenciaviajes.reservas.repository;

import com.agenciaviajes.reservas.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    List<Vuelo> findByPlazasDisponiblesGreaterThan(int i);
}
