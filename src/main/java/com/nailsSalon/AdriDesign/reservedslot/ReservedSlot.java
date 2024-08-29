package com.nailsSalon.AdriDesign.reservedslot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "reservedSlots")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservedSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDate date;
    private LocalTime time;


}
