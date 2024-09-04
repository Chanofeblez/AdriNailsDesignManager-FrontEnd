package com.nailsSalon.AdriDesign.course;

import com.nailsSalon.AdriDesign.video.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable UUID id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/videos")
    public ResponseEntity<List<Video>> accessCourseVideos(@PathVariable UUID id, @RequestParam UUID userId) {
        if (courseService.verifyPayment(id, userId)) {
            Optional<Course> course = courseService.getCourseById(id);
            return course.map(c -> ResponseEntity.ok(c.getVideos())).orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(403).build(); // 403 Forbidden if the user hasn't paid
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable UUID id, @RequestBody Course course) {
        Optional<Course> existingCourse = courseService.getCourseById(id);
        if (existingCourse.isPresent()) {
            course.setId(id);
            return ResponseEntity.ok(courseService.updateCourse(course));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
