package com.agenciaviajes.reservas.controller;

import com.agenciaviajes.reservas.model.Hotel;
import com.agenciaviajes.reservas.model.Reserva;
import com.agenciaviajes.reservas.model.Vuelo;
import com.agenciaviajes.reservas.service.HotelService;
import com.agenciaviajes.reservas.service.ReservaService;
import com.agenciaviajes.reservas.service.VueloService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private VueloService vueloService;

    @Autowired
    private HotelService hotelService;

    @PostConstruct
    public void init() {
        if (reservaService.listarReservas().isEmpty()) {
            Vuelo vuelo1 = vueloService.buscarPorId(1L);
            Hotel hotel1 = hotelService.buscarPorId(1L);

            Vuelo vuelo2 = vueloService.buscarPorId(2L);
            Hotel hotel2 = hotelService.buscarPorId(2L);

            if (vuelo1 != null && hotel1 != null) {
                Reserva r1 = new Reserva();
                r1.setUsuario("Juan Perez");
                r1.setDni("12345678A");
                r1.setVueloAsociado(vuelo1);
                r1.setHotelAsociado(hotel1);
                reservaService.crearReserva(r1);

            }

            if (vuelo2 != null && hotel2 != null) {
                Reserva r2 = new Reserva();
                r2.setUsuario("Maria Gomez");
                r2.setDni("87654321B");
                r2.setVueloAsociado(vuelo2);
                r2.setHotelAsociado(hotel2);
                reservaService.crearReserva(r2);

            }

            if (vuelo1 != null && hotel2 != null) {
                Reserva r3 = new Reserva();
                r3.setUsuario("Carlos Ruiz");
                r3.setDni("11223344C");
                r3.setVueloAsociado(vuelo1);
                r3.setHotelAsociado(hotel2);
                reservaService.crearReserva(r3);
            }
        }
    }


    // GET /reservas, lista todas las reservas
    @GetMapping
    public List<Reserva> listarReservas() {
        return reservaService.listarReservas();
    }
}
