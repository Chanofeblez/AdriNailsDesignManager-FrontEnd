package com.nailsSalon.AdriDesign.reservedslot;

import com.nailsSalon.AdriDesign.payment.PaymentController;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReservedSlotService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private ReservedSlotRepository reservedSlotRepository;

    public List<LocalTime> getAvailableSlots(LocalDate date, List<LocalTime> allSlots) {
        logger.info("All Slot List: {}", allSlots);
        List<ReservedSlot> reservedSlots = reservedSlotRepository.findByDate(date);
        logger.info("Reserve Slot List: {}", reservedSlots);
        return allSlots.stream()
                .filter(slot -> reservedSlots.stream().noneMatch(rs -> rs.getTime().equals(slot)))
                .collect(Collectors.toList());
    }


    public ReservedSlot reserveSlot(ReservedSlot reservedSlot) {
        return reservedSlotRepository.save(reservedSlot);
    }

    // Método para eliminar un slot reservado
    public void releaseSlot(LocalDate date, LocalTime time) {
        List<ReservedSlot> slots = reservedSlotRepository.findByDateAndTime(date, time);

        if (!slots.isEmpty()) {
            // Eliminar todos los slots coincidentes
            reservedSlotRepository.deleteAll(slots);
        }
    }

    // Nuevo método para verificar si un slot está disponible
    public boolean isSlotAvailable(LocalDate date, LocalTime time) {
        List<ReservedSlot> reservedSlots = reservedSlotRepository.findByDateAndTime(date, time);
        return reservedSlots.isEmpty();
    }

}

