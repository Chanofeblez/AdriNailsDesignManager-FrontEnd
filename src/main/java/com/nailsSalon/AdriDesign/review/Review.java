package com.nailsSalon.AdriDesign.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nailsSalon.AdriDesign.appointment.Appointment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Usa AUTO en lugar de IDENTITY para UUID
    private UUID id;

    @Lob
    @Column(name = "customer_photo")
    private byte[] customerPhoto;

    private Integer rating;

    @Column(length = 255)
    private String reviewText;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    @ToString.Exclude  // Evitar recursión infinita en el método toString()
    @JsonBackReference  // Evitar problemas de serialización cíclica
    private Appointment appointment;

    // Getters y setters...
}
