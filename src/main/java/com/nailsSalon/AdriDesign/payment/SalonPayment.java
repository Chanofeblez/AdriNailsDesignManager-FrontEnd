package com.nailsSalon.AdriDesign.payment;

import com.nailsSalon.AdriDesign.appointment.Appointment;
import com.nailsSalon.AdriDesign.course.Course;
import com.nailsSalon.AdriDesign.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
public class SalonPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = true)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = true)
    private Course course; // Para pagos de cursos

    @Column(nullable = false)
    private String paymentMethodId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructor por defecto
    public SalonPayment() {}

    // Constructor para pagos de citas (Appointment)
    public SalonPayment(Customer customer, Appointment appointment, String paymentMethodId, Long amount, String currency, PaymentStatus status) {
        this.customer = customer;
        this.appointment = appointment;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }

    // Constructor para pagos de cursos (Course)
    public SalonPayment(Customer customer, Course course, String paymentMethodId, Long amount, String currency, PaymentStatus status) {
        this.customer = customer;
        this.course = course;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }

    // Enumeración para el estado del pago
    public enum PaymentStatus {
        COMPLETED, PENDING, FAILED
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }
}



