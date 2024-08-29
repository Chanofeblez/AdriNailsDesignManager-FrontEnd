package com.nailsSalon.AdriDesign.appointment;

import com.nailsSalon.AdriDesign.customer.Customer;
import com.nailsSalon.AdriDesign.customer.CustomerRepository;
import com.nailsSalon.AdriDesign.customer.CustomerService;
import com.nailsSalon.AdriDesign.dto.AppointmentRequestDTO;
import com.nailsSalon.AdriDesign.exception.ResourceNotFoundException;
import com.nailsSalon.AdriDesign.payment.PaymentController;
import com.nailsSalon.AdriDesign.reservedslot.ReservedSlot;
import com.nailsSalon.AdriDesign.reservedslot.ReservedSlotRepository;
import com.nailsSalon.AdriDesign.servicio.Servicio;
import com.nailsSalon.AdriDesign.servicio.ServicioRepository;
import com.nailsSalon.AdriDesign.servicio.ServicioService;
import com.nailsSalon.AdriDesign.serviciovariant.ServicioVariant;
import com.nailsSalon.AdriDesign.serviciovariant.ServicioVariantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;

    private final ServicioRepository servicioRepository;

    private final ServicioVariantRepository servicioVariantRepository;
    private final ReservedSlotRepository reservedSlotRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository,
                              ServicioRepository servicioRepository, ServicioVariantRepository servicioVariantRepository,
                              ReservedSlotRepository reservedSlotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.servicioRepository = servicioRepository;
        this.servicioVariantRepository = servicioVariantRepository;
        this.reservedSlotRepository = reservedSlotRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByUserId(String userId) {
        logger.info("user2", userId);
        return appointmentRepository.findByCustomerEmail(userId);
    }

    public Appointment createAppointment(String customerEmail, String serviceName, List<UUID> variantIds,
                                         String date, String time, BigDecimal totalCost,
                                         AppointmentStatus status) {
        logger.info("Dentro del servicio");
        Appointment appointment = new Appointment();
        List<String> uuidStrings = variantIds.stream()
                .map(UUID::toString)
                .collect(Collectors.toList());

        logger.info("antes de crear appointmet: {}", uuidStrings);
        String timeString = time + ":00"; // convierte el número a "03:00"
        logger.info("antes de crear appointmet: {}", timeString);

        // Asignar los valores
        appointment.setCustomerEmail(customerEmail);
        logger.info("email: {}", appointment.getCustomerEmail());
        appointment.setServiceName(serviceName);
        logger.info("servicio: {}", appointment.getServiceName());
        appointment.setServiceVariantIds(uuidStrings);
        logger.info("variantes: {}", appointment.getServiceVariantIds());
        appointment.setAppointmentDate(LocalDate.parse(date));
        logger.info("date: {}", appointment.getAppointmentDate());
        appointment.setAppointmentTime(LocalTime.parse(timeString));
        logger.info("time: {}", appointment.getAppointmentTime());
        appointment.setTotalCost(totalCost);
        logger.info("costo: {}", appointment.getTotalCost());
        appointment.setStatus(status);
        logger.info("status: {}", appointment.getStatus());

        logger.info("AppointmentInService: {}", appointment);

        ReservedSlot reservedSlot = new ReservedSlot();
        reservedSlot.setDate(appointment.getAppointmentDate());
        reservedSlot.setTime(appointment.getAppointmentTime());

        reservedSlotRepository.save(reservedSlot);

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(UUID id, AppointmentRequestDTO appointmentRequestDTO) {
        return appointmentRepository.findById(id).map(appointment -> {
            // Actualizar la fecha y hora
                appointment.setAppointmentDate(LocalDate.parse(appointmentRequestDTO.getAppointmentDate()));
                appointment.setAppointmentTime(LocalTime.parse(appointmentRequestDTO.getAppointmentTime()));
                appointment.setTotalCost(appointmentRequestDTO.getTotalCost());

            // Actualizar el servicio si se proporcionó un nuevo ID de servicio
            if (appointmentRequestDTO.getServiceName() != null) {
                Servicio service = servicioRepository.findByName(appointmentRequestDTO.getServiceName())
                        .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + appointmentRequestDTO.getServiceName()));
                appointment.setServiceName(appointmentRequestDTO.getServiceName());
            }

            // Actualizar los variantes de servicio si se proporcionaron
            if (appointmentRequestDTO.getServiceVariantIds() != null && !appointmentRequestDTO.getServiceVariantIds().isEmpty()) {
                List<ServicioVariant> serviceVariants = servicioVariantRepository.findAllById(appointmentRequestDTO.getServiceVariantIds());
               // appointment.setServiceVariants(serviceVariants);
            }

            // Guardar y devolver la cita actualizada
            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
    }


    public void deleteAppointment(UUID id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with id " + id);
        }
        appointmentRepository.deleteById(id);
    }

    public Appointment updateAppointmentStatus(UUID appointmentId, AppointmentStatus status) {
        // Buscar la cita en la base de datos
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));

        // Actualizar el estado
        appointment.setStatus(status);

        // Guardar la cita actualizada en la base de datos
        return appointmentRepository.save(appointment);
    }


}
