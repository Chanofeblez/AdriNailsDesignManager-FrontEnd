package com.nailsSalon.AdriDesign.course;

import com.nailsSalon.AdriDesign.video.Video;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    private String description;

    private BigDecimal price;

    // Campo para almacenar la URL o ruta de la imagen de presentación
    private String imageUrl;

    // Campo para almacenar la URL o ruta del archivo PDF (teoría)
    private String pdfUrl;

    // Relación de uno a muchos con la entidad Video
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Video> videos;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    // Getters and Setters
}

enum CourseStatus {
    ACTIVE, INACTIVE
}

