package com.agenciaviajes.reservas.init;

import com.agenciaviajes.reservas.model.Hotel;
import com.agenciaviajes.reservas.model.Reserva;
import com.agenciaviajes.reservas.model.Vuelo;
import com.agenciaviajes.reservas.service.HotelService;
import com.agenciaviajes.reservas.service.ReservaService;
import com.agenciaviajes.reservas.service.VueloService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer {

    @Autowired
    private VueloService vueloService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ReservaService reservaService;

    @PostConstruct
    public void init() {
        // Solo carga si no hay datos para evitar duplicados
        if (vueloService.listarVuelos().isEmpty() && hotelService.listarHoteles().isEmpty() && reservaService.listarReservas().isEmpty()) {

            Vuelo vuelo1 = new Vuelo();
            vuelo1.setCompania("Iberia");
            vuelo1.setFecha(LocalDate.of(2025, 6, 15));
            vuelo1.setPrecio(new BigDecimal("150.00"));
            vuelo1.setPlazasDisponibles(100);
            vueloService.guardarVuelo(vuelo1);

            Vuelo vuelo2 = new Vuelo();
            vuelo2.setCompania("Air Europa");
            vuelo2.setFecha(LocalDate.of(2025, 7, 10));
            vuelo2.setPrecio(new BigDecimal("120.00"));
            vuelo2.setPlazasDisponibles(80);
            vueloService.guardarVuelo(vuelo2);

            Hotel hotel1 = new Hotel();
            hotel1.setNombre("Hotel Sol");
            hotel1.setCategoria("4 estrellas");
            hotel1.setPrecio(new BigDecimal("80.00"));
            hotel1.setDisponibilidad(true);
            hotelService.guardarHotel(hotel1);

            Hotel hotel2 = new Hotel();
            hotel2.setNombre("Hotel Luna");
            hotel2.setCategoria("3 estrellas");
            hotel2.setPrecio(new BigDecimal("60.00"));
            hotel2.setDisponibilidad(true);
            hotelService.guardarHotel(hotel2);


            Reserva r1 = new Reserva();
            r1.setUsuario("Juan Perez");
            r1.setDni("12345678A");
            r1.setVueloAsociado(vuelo1);
            r1.setHotelAsociado(hotel1);
            reservaService.guardarReserva(r1);

            Reserva r2 = new Reserva();
            r2.setUsuario("Maria Gomez");
            r2.setDni("87654321B");
            r2.setVueloAsociado(vuelo2);
            r2.setHotelAsociado(hotel2);
            reservaService.guardarReserva(r2);


            System.out.println("Datos iniciales cargados correctamente.");
        }
    }
}
