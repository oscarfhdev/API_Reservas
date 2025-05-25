package com.agenciaviajes.reservas.service;

import com.agenciaviajes.reservas.model.Hotel;
import com.agenciaviajes.reservas.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> listarHoteles() {
        return hotelRepository.findAll();
    }

    public List<Hotel> listarHotelesDisponibles() {
        return hotelRepository.findByDisponibilidadTrue();
    }

    public Hotel buscarPorId(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    public Hotel guardarHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public void eliminarHotel(Long id) {
        hotelRepository.deleteById(id);
    }
}