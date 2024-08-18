package com.nailsSalon.AdriDesign.appointment;

import com.nailsSalon.AdriDesign.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepository.findById(id);
    }

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(UUID id, Appointment appointmentDetails) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setCustomer(appointmentDetails.getCustomer());
            appointment.setService(appointmentDetails.getService());
            appointment.setServiceVariant(appointmentDetails.getServiceVariant());
            appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
            appointment.setConfirmed(appointmentDetails.isConfirmed());
            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
    }

    public void deleteAppointment(UUID id) {
        appointmentRepository.deleteById(id);
    }
}
