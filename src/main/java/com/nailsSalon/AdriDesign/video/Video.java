package com.nailsSalon.AdriDesign.video;

import com.nailsSalon.AdriDesign.course.Course;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Video {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String url;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Getters and Setters
}
