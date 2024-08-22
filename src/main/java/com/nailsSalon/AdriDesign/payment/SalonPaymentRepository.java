package com.nailsSalon.AdriDesign.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SalonPaymentRepository extends JpaRepository<SalonPayment, UUID> {
}
