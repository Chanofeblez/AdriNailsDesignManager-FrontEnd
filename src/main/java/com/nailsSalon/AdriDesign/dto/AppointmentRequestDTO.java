package com.nailsSalon.AdriDesign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nailsSalon.AdriDesign.appointment.AppointmentStatus;
import com.nailsSalon.AdriDesign.customer.Customer;
import com.nailsSalon.AdriDesign.serviciovariant.ServicioVariant;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class AppointmentRequestDTO {

    @JsonProperty("customerEmail")
    private String customerEmail;  // UUID del usuario

    @JsonProperty("serviceName")
    private String serviceName;  // UUID del servicio

    @JsonProperty("serviceVariantIds")
    private List<UUID> serviceVariantIds;  // Lista de UUIDs de las variantes seleccionadas

    @JsonProperty("appointmentDate")
    private String appointmentDate; // Formato "yyyy-MM-dd"

    @JsonProperty("appointmentTime")
    private String appointmentTime; // Formato "HH:mm:ss"

    @JsonProperty("totalCost")
    private BigDecimal totalCost; // Costo total

    @JsonProperty("status")
    private AppointmentStatus status; // Estado de la cita

    @JsonProperty("imagePath")
    private String imagePath; // Direccion de la imagen del servicio
}

