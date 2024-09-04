package com.nailsSalon.AdriDesign.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    // Métodos personalizados de búsqueda si es necesario
}
