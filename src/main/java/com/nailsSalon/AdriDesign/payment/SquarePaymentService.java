package com.nailsSalon.AdriDesign.payment;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.Money;
import com.squareup.square.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SquarePaymentService {

    private final SquareClient squareClient;

    @Autowired
    public SquarePaymentService(@Value("${square.access.token}") String accessToken) {
        this.squareClient = new SquareClient.Builder()
                .accessToken(accessToken)
                .environment(Environment.SANDBOX) // Cambia a "production" para producción
                .build();
    }

    public SquareClient getSquareClient() {
        return squareClient;
    }

    public Payment createPayment(String sourceId, String idempotencyKey, Money amountMoney, String customerId, String locationId) throws ApiException, IOException {
        PaymentsApi paymentsApi = squareClient.getPaymentsApi();

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest.Builder(sourceId, idempotencyKey) // sourceId e idempotencyKey son parámetros del método
                .amountMoney(amountMoney) // Especificamos la cantidad del pago
                .autocomplete(true) // Autocompletar el pago
                .customerId(customerId) // Ahora es un parámetro del método
                .locationId(locationId) // Ahora es un parámetro del método
                .referenceId("123456") // Un ID de referencia, puede ser cualquier cadena que sirva para identificar la transacción
                .build();

        return paymentsApi.createPayment(createPaymentRequest).getPayment();
    }
}
