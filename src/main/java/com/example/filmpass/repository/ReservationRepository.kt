package com.example.filmpass.repository

import com.example.filmpass.entity.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReservationRepository : JpaRepository<Reservation?, Long?> {
    fun findBySeatSeatId(seatId: Long?): Optional<Reservation?>?
    fun findByReserveId(reserveId: Long?): Reservation?
}
