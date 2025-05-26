package com.agenciaviajes.reservas.controller;

import com.agenciaviajes.reservas.model.Vuelo;
import com.agenciaviajes.reservas.service.VueloService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vuelos")
public class VueloController {

    @Autowired
    private VueloService vueloService;

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
    public ResponseEntity<?> eliminarVuelo(@PathVariable Long id) {
        try {
            vueloService.eliminarVuelo(id);
            return ResponseEntity.ok().build();  // 200 OK sin contenido
        } catch (Exception e) {
            // Aquí identificamos el tipo de excepción para dar mensajes más específicos
            if (e instanceof DataIntegrityViolationException) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "No se puede eliminar el vuelo porque está vinculado a una reserva"));
            }
            // Si no es el caso, mensaje genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error interno al vuelo el hotel"));
        }
    }

}
