package com.nailsSalon.AdriDesign.course;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String url;  // URL o ruta del video

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;  // Relación con el curso

    // Otros campos opcionales como título o descripción
}
