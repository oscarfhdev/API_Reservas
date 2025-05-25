package com.agenciaviajes.reservas.controller;

import com.agenciaviajes.reservas.model.Vuelo;
import com.agenciaviajes.reservas.service.VueloService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vuelos")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @PostConstruct
    public void init() {
        if (vueloService.listarVuelos().isEmpty()) {
            Vuelo v1 = new Vuelo();
            v1.setCompania("Iberia");
            v1.setFecha(LocalDate.of(2025, 6, 10));
            v1.setPrecio(new BigDecimal("150.00"));
            v1.setPlazasDisponibles(50);

            Vuelo v2 = new Vuelo();
            v2.setCompania("Ryanair");
            v2.setFecha(LocalDate.of(2025, 7, 5));
            v2.setPrecio(new BigDecimal("90.00"));
            v2.setPlazasDisponibles(30);

            vueloService.guardarVuelo(v1);
            vueloService.guardarVuelo(v2);
        }
    }

    // GET /vuelos, lista todos los vuelos
    @GetMapping
    public List<Vuelo> listarVuelos() {
        return vueloService.listarVuelos();
    }

    // GET /vuelos/disponibles, lista los que están disponibles

    @GetMapping("/disponibles")
    public List<Vuelo> listarVuelosDisponibles() {
        return vueloService.listarVuelosDisponibles();
    }

    // POST /vuelos, crea un vuelo nuevo
    @PostMapping
    public Vuelo crearVuelo(@RequestBody Vuelo vuelo) {
        return vueloService.guardarVuelo(vuelo);
    }

    // PUT /vuelos/{id}, actualiza un vuelo
    @PutMapping("/{id}")
    public Vuelo actualizarVuelo(@PathVariable Long id, @RequestBody Vuelo vueloActualizado) {
        Vuelo vueloExistente = vueloService.buscarPorId(id);
        if (vueloExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vuelo no encontrado");
        }

        vueloExistente.setCompania(vueloActualizado.getCompania());
        vueloExistente.setFecha(vueloActualizado.getFecha());
        vueloExistente.setPrecio(vueloActualizado.getPrecio());
        vueloExistente.setPlazasDisponibles(vueloActualizado.getPlazasDisponibles());

        return vueloService.guardarVuelo(vueloExistente);
    }

    // DELETE /vuelos/{id}, elimina un vuelo
    @DeleteMapping("/{id}")
    public void eliminarVuelo(@PathVariable Long id) {
        vueloService.eliminarVuelo(id);
    }
}
