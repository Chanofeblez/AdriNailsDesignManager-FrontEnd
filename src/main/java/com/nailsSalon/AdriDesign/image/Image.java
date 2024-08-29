package com.nailsSalon.AdriDesign.image;

import jakarta.persistence.*;
import lombok.Data;

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

    // Constructor
    public Image() {}

    public Image(String title, byte[] data, String contentType) {
        this.title = title;
        this.data = data;
        this.contentType = contentType;
    }


}
