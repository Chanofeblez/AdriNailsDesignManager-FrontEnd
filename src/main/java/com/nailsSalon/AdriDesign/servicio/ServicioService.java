package com.nailsSalon.AdriDesign.servicio;

import com.nailsSalon.AdriDesign.exception.ResourceNotFoundException;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServicioService {


    private final ServicioRepository servicioRepository;

    @Autowired
    public ServicioService(ServicioRepository serviceRepository) {
        this.servicioRepository = serviceRepository;
    }

    public List<Servicio> getAllServices() {
        return servicioRepository.findAll();
    }

    public Optional<Servicio> getServiceById(UUID id) {
        return servicioRepository.findById(id);
    }

    public Servicio createService(Servicio service) {
        return servicioRepository.save(service);
    }

    public Servicio updateService(UUID id, Servicio serviceDetails) {
        return servicioRepository.findById(id).map(service -> {
            service.setName(serviceDetails.getName());
            service.setDescription(serviceDetails.getDescription());
            service.setPrice(serviceDetails.getPrice());
            service.setType(serviceDetails.getType());
            return servicioRepository.save(service);
        }).orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + id));
    }

    public void deleteService(UUID id) {
        servicioRepository.deleteById(id);
    }
}
