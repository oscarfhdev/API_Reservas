package com.agenciaviajes.reservas.service;

import com.agenciaviajes.reservas.model.Vuelo;
import com.agenciaviajes.reservas.repository.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VueloService {

    @Autowired
    private VueloRepository vueloRepository;

    public List<Vuelo> listarVuelos() {
        return vueloRepository.findAll();
    }

    public List<Vuelo> listarVuelosDisponibles() {
        return vueloRepository.findAll()
                .stream()
                .filter(v -> v.getPlazasDisponibles() > 0)
                .toList();
    }

    public Vuelo buscarPorId(Long id) {
        return vueloRepository.findById(id).orElse(null);
    }

    public Vuelo guardarVuelo(Vuelo vuelo) {
        return vueloRepository.save(vuelo);
    }

    public void eliminarVuelo(Long id) {
        vueloRepository.deleteById(id);
    }
}