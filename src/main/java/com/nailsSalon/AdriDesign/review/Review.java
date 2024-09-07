package com.nailsSalon.AdriDesign.review;

import com.nailsSalon.AdriDesign.appointment.Appointment;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Usa AUTO en lugar de IDENTITY para UUID
    private UUID id;

    @Lob
    private byte[] customerPhoto;

    private Integer rating;

    @Column(length = 255)
    private String reviewText;

    // Getters y setters...
}
