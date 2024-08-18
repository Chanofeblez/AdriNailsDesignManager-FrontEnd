package com.nailsSalon.AdriDesign.payment;

import lombok.Data;

@Data
public class PaymentRequest {

    private String sourceId; // El ID de la fuente (token de tarjeta)
    private long amount; // Cantidad en centavos
    private String customerId;
    private String locationId;
}
