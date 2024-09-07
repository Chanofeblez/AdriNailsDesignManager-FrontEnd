package com.nailsSalon.AdriDesign.reservedslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservedSlotRepository extends JpaRepository<ReservedSlot, UUID> {
    List<ReservedSlot> findByDate(LocalDate date);

    List<ReservedSlot> findByDateAndTime(LocalDate date, LocalTime time);
}
