package com.example.filmpass.repository

import com.example.filmpass.entity.Payment
import com.example.filmpass.entity.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PaymentRepository : JpaRepository<Payment?, Long?> {
    fun findTopByOrderByCreatedTsDesc(): Optional<Payment?>?
    fun findByReservation(reservation: Reservation?): Payment?
}
