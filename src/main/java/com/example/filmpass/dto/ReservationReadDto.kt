package com.example.filmpass.dto

import com.example.filmpass.entity.Reservation
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor


class ReservationReadDto(reservation: Reservation) {
    private val userId = reservation.member!!.memberId
    private val reservationDate = reservation.bookingDate
    private val seatId: Long? = reservation.seat!!.seatId
    private val cinemaMovieName: String? = reservation.cinemaMovie?.movie?.movieName
    private val reservationId = reservation.reserveId
    private val adult = reservation.adult
    private val child = reservation.child
    private val youth = reservation.youth
}
