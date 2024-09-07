package com.nailsSalon.AdriDesign.appointment;

import com.nailsSalon.AdriDesign.dto.AppointmentRequestDTO;
import com.nailsSalon.AdriDesign.exception.ResourceNotFoundException;
import com.nailsSalon.AdriDesign.payment.PaymentController;
import com.nailsSalon.AdriDesign.review.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:8100")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByUser(@PathVariable String userId) {
        logger.info("user", userId);
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(userId);
        logger.info("appointment", appointments);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequestDTO appointmentDTO) {
        logger.info("Appointment Request DTO: {}", appointmentDTO);
        try {
            // Asume que el DTO contiene los IDs necesarios
            String customerEmail = appointmentDTO.getCustomerEmail();
            logger.info("Captura de email1: {}", appointmentDTO.getCustomerEmail());
            logger.info("Captura de email2: {}", customerEmail);
            String serviceName = appointmentDTO.getServiceName();
            List<UUID> serviceVariantIds = appointmentDTO.getServiceVariantIds();

            logger.info("Antes del servicio");

            // Llama al servicio para crear el appointment pasando los IDs
            Appointment createdAppointment = appointmentService.createAppointment(customerEmail, serviceName, serviceVariantIds,
                    appointmentDTO.getAppointmentDate(), appointmentDTO.getAppointmentTime(),
                    appointmentDTO.getTotalCost(), appointmentDTO.getStatus(), appointmentDTO.getImagePath());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating appointment");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody Map<String, String> statusMap) {
        String status = statusMap.get("status");

        if (status == null) {
            return ResponseEntity.badRequest().body(null);
        }

        AppointmentStatus appointmentStatus;

        try {
            System.out.println("try: " + status);
            appointmentStatus = AppointmentStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            System.out.println("Catch: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, appointmentStatus);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable UUID appointmentId,
            @RequestBody Map<String, String> request) {

        String statusString = request.get("status");
        AppointmentStatus status = AppointmentStatus.valueOf(statusString.toUpperCase());
        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<?> addReview(
            @PathVariable UUID id,
            @RequestParam("reviewText") String reviewText,
            @RequestParam("rating") Integer rating,
            @RequestParam("photo") MultipartFile photo) {

        Optional<Appointment> optionalAppointment = appointmentService.getAppointmentById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            Review review = new Review();
            review.setReviewText(reviewText);
            review.setRating(rating);

            try {
                review.setCustomerPhoto(photo.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo.");
            }

            appointment.setReview(review); // Asocia la reseña con el appointment

            // Convertir los valores para llamar correctamente a createAppointment()
            List<UUID> variantUUIDs = appointment.getServiceVariantIds().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());

            appointmentService.createAppointment(
                    appointment.getCustomerEmail(),
                    appointment.getServiceName(),
                    variantUUIDs,
                    appointment.getAppointmentDate().toString(),
                    appointment.getAppointmentTime().toString(),
                    appointment.getTotalCost(),
                    // Si el método ya no necesita AppointmentStatus, elimínalo de la definición
                    AppointmentStatus.COMPLETED, // Si el status es necesario
                    appointment.getImagePath() // Asegúrate de que sea un String, no un Review
            );

            return ResponseEntity.ok("Review and photo added successfully.");
        }
        return ResponseEntity.badRequest().body("Appointment not found.");
    }
}

