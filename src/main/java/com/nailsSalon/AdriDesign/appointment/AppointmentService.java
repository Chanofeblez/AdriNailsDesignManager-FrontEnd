package com.nailsSalon.AdriDesign.appointment;

import com.nailsSalon.AdriDesign.customer.Customer;
import com.nailsSalon.AdriDesign.customer.CustomerRepository;
import com.nailsSalon.AdriDesign.dto.AppointmentRequestDTO;
import com.nailsSalon.AdriDesign.exception.ResourceNotFoundException;
import com.nailsSalon.AdriDesign.servicio.Servicio;
import com.nailsSalon.AdriDesign.servicio.ServicioRepository;
import com.nailsSalon.AdriDesign.serviciovariant.ServicioVariant;
import com.nailsSalon.AdriDesign.serviciovariant.ServicioVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServicioRepository servicioRepository;
    private final ServicioVariantRepository servicioVariantRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, ServicioRepository servicioRepository, ServicioVariantRepository servicioVariantRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.servicioRepository = servicioRepository;
        this.servicioVariantRepository = servicioVariantRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepository.findById(id);
    }

    public Appointment createAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        // Buscar el Customer en la base de datos
        Customer customer = customerRepository.findById(appointmentRequestDTO.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Buscar el Servicio en la base de datos
        Servicio service = servicioRepository.findByName(appointmentRequestDTO.getServiceName())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        // Crear un nuevo Appointment
        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setService(service);
        appointment.setAppointmentDate(LocalDate.parse(appointmentRequestDTO.getDate()));
        appointment.setAppointmentTime(LocalTime.parse(appointmentRequestDTO.getTime()));
        appointment.setTotalCost(BigDecimal.valueOf(appointmentRequestDTO.getTotalCost()));

        // Agregar las variantes de servicio
        List<UUID> variantIds = appointmentRequestDTO.getVariantes().stream()
                .map(ServicioVariant::getId)
                .collect(Collectors.toList());

        List<ServicioVariant> serviceVariants = servicioVariantRepository.findAllById(variantIds);
        appointment.setServiceVariants(serviceVariants);

        // Guardar el Appointment en la base de datos
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(UUID id, AppointmentRequestDTO appointmentRequestDTO) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setAppointmentDate(LocalDate.parse(appointmentRequestDTO.getDate()));
            appointment.setAppointmentTime(LocalTime.parse(appointmentRequestDTO.getTime()));
            appointment.setTotalCost(BigDecimal.valueOf(appointmentRequestDTO.getTotalCost()));

            List<UUID> variantIds = appointmentRequestDTO.getVariantes().stream()
                    .map(ServicioVariant::getId)
                    .collect(Collectors.toList());

            List<ServicioVariant> serviceVariants = servicioVariantRepository.findAllById(variantIds);
            appointment.setServiceVariants(serviceVariants);

            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
    }

    public void deleteAppointment(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with id " + id);
        }
        appointmentRepository.deleteById(id);
    }


}
