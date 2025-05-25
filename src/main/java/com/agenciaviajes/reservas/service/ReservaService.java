package com.agenciaviajes.reservas.service;

import com.agenciaviajes.reservas.model.Hotel;
import com.agenciaviajes.reservas.model.Reserva;
import com.agenciaviajes.reservas.model.Vuelo;
import com.agenciaviajes.reservas.repository.ReservaRepository;
import com.agenciaviajes.reservas.repository.VueloRepository;
import com.agenciaviajes.reservas.repository.HotelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public Reserva crearReserva(Reserva reserva) {
        // Verificar vuelo
        Vuelo vuelo = vueloRepository.findById(reserva.getVueloAsociado().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vuelo no encontrado"));

        if (vuelo.getPlazasDisponibles() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay plazas disponibles en el vuelo");
        }

        // Verificar hotel
        Hotel hotel = hotelRepository.findById(reserva.getHotelAsociado().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel no encontrado"));

        // Asociar objetos completos a la reserva
        reserva.setVueloAsociado(vuelo);
        reserva.setHotelAsociado(hotel);

        // Si todo se hace bien, se resta 1
        vuelo.setPlazasDisponibles(vuelo.getPlazasDisponibles() - 1);
        vueloRepository.save(vuelo);

        // Guardar reserva
        return reservaRepository.save(reserva);
    }

    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}
