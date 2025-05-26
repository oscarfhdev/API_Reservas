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

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private VueloService vueloService;

    @Autowired
    private HotelService hotelService;

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    // Guardamos reserva
    public Reserva guardarReserva(Reserva reserva) {
        Vuelo vuelo = reserva.getVueloAsociado();
        Hotel hotel = reserva.getHotelAsociado();

        // Validacion
        if (vuelo == null || vuelo.getPlazasDisponibles() <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vuelo no tiene plazas disponibles.");
        }

        //Validación
        if (hotel == null || !hotel.getDisponibilidad()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El vuelo no tiene plazas disponibles.");
        }

        // Lógica de negocio: reducir disponibilidad
        vuelo.setPlazasDisponibles(vuelo.getPlazasDisponibles() - 1);
        vueloService.guardarVuelo(vuelo);

        hotel.setDisponibilidad(false);
        hotelService.guardarHotel(hotel);

        return reservaRepository.save(reserva);
    }

    // Aquí lo creamos todo solo con los datos
    public Reserva crearReservaDesdeIds(String dni, String usuario, Long vueloId, Long hotelId) {
        Vuelo vuelo = vueloRepository.findById(vueloId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vuelo no encontrado"));

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel no encontrado"));

        // Validaciones
        if (vuelo.getPlazasDisponibles() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El vuelo no tiene plazas disponibles");

        }

        if (!hotel.getDisponibilidad()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El hotel no está disponible");
        }

        // Crear y guardar reserva
        Reserva reserva = new Reserva();
        reserva.setDni(dni);
        reserva.setUsuario(usuario);
        reserva.setVueloAsociado(vuelo);
        reserva.setHotelAsociado(hotel);

        // Actualizar disponibilidad
        vuelo.setPlazasDisponibles(vuelo.getPlazasDisponibles() - 1);
        hotel.setDisponibilidad(false);

        vueloService.guardarVuelo(vuelo);
        hotelService.guardarHotel(hotel);

        return reservaRepository.save(reserva);
    }

    // Eliminamos reservas desde ID
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }


    // Actualizamos reservas desde id
    public Reserva actualizarReservaDesdeIds(Long id, String dni, String usuario, Long vueloId, Long hotelId) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

        Vuelo vuelo = vueloRepository.findById(vueloId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vuelo no encontrado"));

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel no encontrado"));

        // Validar plazas solo si el vuelo es diferente al actual
        if (!vuelo.getId().equals(reservaExistente.getVueloAsociado().getId())) {
            if (vuelo.getPlazasDisponibles() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El vuelo no está disponible");
            }
        }

        // Validar disponibilidad solo si el hotel es diferente al actual
        if (!hotel.getId().equals(reservaExistente.getHotelAsociado().getId())) {
            if (!hotel.getDisponibilidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El hotel no está disponible");
            }
        }

        // Actualizar campos
        reservaExistente.setDni(dni);
        reservaExistente.setUsuario(usuario);
        reservaExistente.setVueloAsociado(vuelo);
        reservaExistente.setHotelAsociado(hotel);

        return reservaRepository.save(reservaExistente);
    }
}

