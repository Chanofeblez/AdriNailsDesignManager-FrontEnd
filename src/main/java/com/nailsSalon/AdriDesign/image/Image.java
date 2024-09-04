package com.nailsSalon.AdriDesign.image;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    @Lob
    private byte[] data;

    private String contentType;

    private String type; // Campo para almacenar el tipo de imagen ("Manicure" o "Pedicure")

    // Constructor por defecto
    public Image() {}

    // Constructor con parámetros
    public Image(String title, byte[] data, String contentType, String type) {
        this.title = title;
        this.data = data;
        this.contentType = contentType;
        this.type = type; // Inicializar el campo type
    }
}


