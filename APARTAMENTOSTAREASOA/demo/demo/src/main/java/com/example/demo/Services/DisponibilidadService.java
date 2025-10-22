package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.DisponibilidadModel;
import com.example.demo.Repositories.IDisponibilidadRepository;

@Service
public class DisponibilidadService {
    
    @Autowired
    private IDisponibilidadRepository disponibilidadRepository;

    public List<DisponibilidadModel> getAllDisponibilidades() {
        return disponibilidadRepository.findAll();
    }

    public Optional<DisponibilidadModel> getDisponibilidadById(Long id) {
        return disponibilidadRepository.findById(id);
    }

    public DisponibilidadModel saveDisponibilidad(DisponibilidadModel disponibilidad) {
        return disponibilidadRepository.save(disponibilidad);
    }

    public void deleteDisponibilidad(Long id) {
        disponibilidadRepository.deleteById(id);
    }
    
}
