package com.nailsSalon.AdriDesign.serviciovariant;

import com.nailsSalon.AdriDesign.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServicioVariantService {

    private final ServicioVariantRepository serviceVariantRepository;

    @Autowired
    public ServicioVariantService(ServicioVariantRepository serviceVariantRepository) {
        this.serviceVariantRepository = serviceVariantRepository;
    }

    public List<ServicioVariant> getAllServiceVariants() {
        return serviceVariantRepository.findAll();
    }

    public Optional<ServicioVariant> getServiceVariantById(UUID id) {
        return serviceVariantRepository.findById(id);
    }

    public ServicioVariant createServiceVariant(ServicioVariant serviceVariant) {
        return serviceVariantRepository.save(serviceVariant);
    }

    public ServicioVariant updateServiceVariant(UUID id, ServicioVariant serviceVariantDetails) {
        return serviceVariantRepository.findById(id).map(serviceVariant -> {
            serviceVariant.setName(serviceVariantDetails.getName());
            serviceVariant.setDescription(serviceVariantDetails.getDescription());
            serviceVariant.setPrice(serviceVariantDetails.getPrice());
            serviceVariant.setService(serviceVariantDetails.getService());
            return serviceVariantRepository.save(serviceVariant);
        }).orElseThrow(() -> new ResourceNotFoundException("ServiceVariant not found with id " + id));
    }

    public void deleteServiceVariant(UUID id) {
        serviceVariantRepository.deleteById(id);
    }

}
