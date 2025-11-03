package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.PropiedadImagenesModel;
import com.example.demo.Repositories.IPropiedadImagenesRepository;

@Service
public class PropiedadImagenesService {
    
    @Autowired
    private IPropiedadImagenesRepository propiedadesImagenesRepository;

    public List<PropiedadImagenesModel> getAllImagenes() {
        return propiedadesImagenesRepository.findAll();
    }

    public Optional<PropiedadImagenesModel> getImagenById(Long id) {
        return propiedadesImagenesRepository.findById(id);
    }

    public PropiedadImagenesModel saveImagen(PropiedadImagenesModel imagen) {
        return propiedadesImagenesRepository.save(imagen);
    }

    public void deleteImagen(Long id) {
        propiedadesImagenesRepository.deleteById(id);
    }

}
