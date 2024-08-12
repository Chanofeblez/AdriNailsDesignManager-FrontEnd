package com.nailsSalon.AdriDesign.serviciovariant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ServicioVariantRepository extends JpaRepository<ServicioVariant, UUID> {
}
