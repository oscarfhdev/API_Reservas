package com.agenciaviajes.reservas.controller;

import com.agenciaviajes.reservas.model.Hotel;
import com.agenciaviajes.reservas.service.HotelService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/hoteles")
public class HotelController {

    @Autowired
    private HotelService hotelService;


    @PostConstruct
    public void init() {
        // Solo si no hay hoteles para no duplicar
        if (hotelService.listarHoteles().isEmpty()) {
            Hotel h1 = new Hotel();
            h1.setNombre("Hotel Plaza");
            h1.setCategoria("4 estrellas");
            h1.setPrecio(new BigDecimal("100.00"));
            h1.setDisponibilidad(true);

            Hotel h2 = new Hotel();
            h2.setNombre("Hotel Costa");
            h2.setCategoria("3 estrellas");
            h2.setPrecio(new BigDecimal("80.00"));
            h2.setDisponibilidad(false);

            hotelService.guardarHotel(h1);
            hotelService.guardarHotel(h2);
        }
    }

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
    public void eliminarHotel(@PathVariable Long id) {
        hotelService.eliminarHotel(id);
    }
}