package com.agenciaviajes.reservas.controller;

import com.agenciaviajes.reservas.model.Hotel;
import com.agenciaviajes.reservas.service.HotelService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hoteles")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // GET /hoteles, nos muestra los hoteles
    @GetMapping
    public List<Hotel> listarTodos() {
        return hotelService.listarHoteles();
    }

    // GET /hoteles/disponibles, nos muestra los hoteles disponibles
    @GetMapping("/disponibles")
    public List<Hotel> listarDisponibles() {
        return hotelService.listarHotelesDisponibles();
    }

    // POST /hoteles, guardamos un hotel nuevo
    @PostMapping
    public Hotel crearHotel(@RequestBody Hotel hotel) {
        return hotelService.guardarHotel(hotel);
    }

    // PUT /hoteles/{id}, actualizamos un hotel por id
    @PutMapping("/{id}")
    public Hotel actualizarHotel(@PathVariable Long id, @RequestBody Hotel hotelActualizado) {
        // Primero buscamos el hotel por id
        Hotel hotelExistente = hotelService.buscarPorId(id);

        if (hotelExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel no encontrado");
        }

        // Actualizamos los campos que quieras
        hotelExistente.setNombre(hotelActualizado.getNombre());
        hotelExistente.setCategoria(hotelActualizado.getCategoria());
        hotelExistente.setPrecio(hotelActualizado.getPrecio());
        hotelExistente.setDisponibilidad(hotelActualizado.getDisponibilidad());

        // Guardamos el hotel actualizado
        return hotelService.guardarHotel(hotelExistente);
    }

    // DELETE /hoteles/{id}, eliminamos el hotel por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHotel(@PathVariable Long id) {
        try {
            hotelService.eliminarHotel(id);
            return ResponseEntity.ok().build();  // 200 OK sin contenido
        } catch (Exception e) {
            // Aquí identificamos el tipo de excepción para dar mensajes más específicos
            if (e instanceof DataIntegrityViolationException) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "No se puede eliminar el hotel porque está vinculado a una reserva"));
            }
            // Si no es el caso, mensaje genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error interno al eliminar el hotel"));
        }
    }

}