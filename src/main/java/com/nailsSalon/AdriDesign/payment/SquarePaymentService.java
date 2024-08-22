package com.nailsSalon.AdriDesign.payment;

import com.nailsSalon.AdriDesign.appointment.Appointment;
import com.nailsSalon.AdriDesign.appointment.AppointmentRepository;
import com.nailsSalon.AdriDesign.customer.CustomerRepository;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CreatePaymentRequest;
import com.nailsSalon.AdriDesign.customer.Customer;
import com.squareup.square.models.Money;
import com.squareup.square.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class SquarePaymentService {

    private final SquareClient squareClient;
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final SalonPaymentRepository salonPaymentRepository;

    @Autowired
    public SquarePaymentService(@Value("${square.access.token}") String accessToken,
                                CustomerRepository customerRepository,
                                AppointmentRepository appointmentRepository,
                                SalonPaymentRepository salonPaymentRepository) {
        this.squareClient = new SquareClient.Builder()
                .accessToken(accessToken)
                .environment(Environment.SANDBOX) // Cambia a "production" para producción
                .build();
        this.customerRepository = customerRepository;
        this.appointmentRepository = appointmentRepository;
        this.salonPaymentRepository = salonPaymentRepository;
    }

    public SquareClient getSquareClient() {
        return squareClient;
    }

    public SalonPayment createPayment(String sourceId, String idempotencyKey, Money amountMoney, String customerId, String locationId, UUID appointmentId) throws ApiException, IOException {
        PaymentsApi paymentsApi = squareClient.getPaymentsApi();

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest.Builder(sourceId, idempotencyKey)
                .amountMoney(amountMoney)
                .autocomplete(true)
                .customerId(customerId)
                .locationId(locationId)
                .referenceId("123456")
                .build();

        com.squareup.square.models.Payment squarePayment = paymentsApi.createPayment(createPaymentRequest).getPayment();

        // Cargar los objetos Customer y Appointment desde la base de datos
        Customer customer = customerRepository.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        // Guardar el pago en la base de datos
        SalonPayment salonPayment = new SalonPayment();
        salonPayment.setCustomer(customer);
        salonPayment.setAppointment(appointment);
        salonPayment.setPaymentMethodId(sourceId);
        salonPayment.setAmount(amountMoney.getAmount());
        salonPayment.setCurrency(amountMoney.getCurrency());
        salonPayment.setStatus(SalonPayment.PaymentStatus.COMPLETED);

        return salonPaymentRepository.save(salonPayment);
    }

}

