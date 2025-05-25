package com.agenciaviajes.reservas.controller;

import com.agenciaviajes.reservas.model.Reserva;
import com.agenciaviajes.reservas.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // GET /reservas, lista todas las reservas
    @GetMapping
    public List<Reserva> listarReservas() {
        return reservaService.listarReservas();
    }

    // POST: Crear nueva reserva pasando solo los datos mínimos
    @PostMapping
    public Reserva crearReserva(@RequestBody Map<String, Object> datos) {
        try {
            String dni = (String) datos.get("dni");
            String usuario = (String) datos.get("usuario");
            Long vueloId = Long.valueOf(datos.get("vueloId").toString());
            Long hotelId = Long.valueOf(datos.get("hotelId").toString());

            return reservaService.crearReservaDesdeIds(dni, usuario, vueloId, hotelId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos inválidos o incompletos");
        }
    }

    // POST: Actualizar una nueva reserva pasando solo los datos mínimos
    @PutMapping("/{id}")
    public Reserva actualizarReserva(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        try {
            String dni = (String) datos.get("dni");
            String usuario = (String) datos.get("usuario");
            Long vueloId = Long.valueOf(datos.get("vueloId").toString());
            Long hotelId = Long.valueOf(datos.get("hotelId").toString());

            return reservaService.actualizarReservaDesdeIds(id, dni, usuario, vueloId, hotelId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos inválidos o incompletos");
        }
    }


    // DELETE /reserva/{id}, eliminamos la reserva por id
    @DeleteMapping("/{id}")
    public void eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
    }
}
