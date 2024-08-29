package com.nailsSalon.AdriDesign.appointment;

import com.nailsSalon.AdriDesign.dto.AppointmentRequestDTO;
import com.nailsSalon.AdriDesign.exception.ResourceNotFoundException;
import com.nailsSalon.AdriDesign.payment.PaymentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
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
                    appointmentDTO.getTotalCost(), appointmentDTO.getStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating appointment");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentRequestDTO appointmentDetails) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, appointmentDetails);
            return ResponseEntity.ok(updatedAppointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
}

