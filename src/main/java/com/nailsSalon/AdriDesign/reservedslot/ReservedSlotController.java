package com.nailsSalon.AdriDesign.reservedslot;

import com.nailsSalon.AdriDesign.payment.PaymentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class ReservedSlotController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private ReservedSlotService reservedSlotService;

    // Endpoint para obtener los slots disponibles en una fecha específica
    @GetMapping("/available")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Aquí puedes definir todos los slots posibles para el día
        List<LocalTime> morningSlots = List.of(
                LocalTime.of(10, 0)
                // Agrega más horarios AM si es necesario
        );
        // Define los slots de la tarde (PM)
        List<LocalTime> afternoonSlots = List.of(
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0)
        );
        // Combina ambas listas si quieres manejar los slots juntos
        List<LocalTime> allSlots = new ArrayList<>();
        allSlots.addAll(morningSlots);
        allSlots.addAll(afternoonSlots);

        // Llama al servicio para obtener los slots disponibles
        List<LocalTime> availableSlots = reservedSlotService.getAvailableSlots(date, allSlots);
        logger.info("Available Slot ListController: {}", availableSlots);

        // Retorna los slots disponibles
        return ResponseEntity.ok(availableSlots);
    }

    // Endpoint para reservar un slot
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveSlot(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {

        ReservedSlot reservedSlot = new ReservedSlot();
        reservedSlot.setDate(date);
        reservedSlot.setTime(time);

        reservedSlotService.reserveSlot(reservedSlot);

        return ResponseEntity.ok("Slot reservado con éxito");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkSlotAvailability(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {

        boolean isAvailable = reservedSlotService.isSlotAvailable(date, time);
        return ResponseEntity.ok(isAvailable);
    }
}
