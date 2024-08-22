package com.nailsSalon.AdriDesign.dto;

import lombok.Data;

import java.util.UUID;

import java.util.UUID;

import java.util.UUID;

import java.util.UUID;

public class PaymentRequestDTO {

    private String sourceId; // El ID de la fuente (token de tarjeta)
    private long amount; // Cantidad en centavos
    private String customerId; // ID del cliente
    private String locationId; // ID de la ubicación de Square
    private UUID appointmentId; // ID de la cita relacionada con el pago

    // Getters y Setters

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }
}



