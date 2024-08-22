package com.nailsSalon.AdriDesign.dto;

import com.nailsSalon.AdriDesign.customer.Customer;
import com.nailsSalon.AdriDesign.serviciovariant.ServicioVariant;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentRequestDTO {

    private Customer user; // Asegúrate de que el objeto esté correctamente serializado/deserializado
    private String date; // Formato "yyyy-MM-dd"
    private String time; // Formato "HH:mm:ss"
    private String serviceName;
    private List<ServicioVariant> variantes; // Lista de variantes seleccionadas
    private double totalCost;

}
