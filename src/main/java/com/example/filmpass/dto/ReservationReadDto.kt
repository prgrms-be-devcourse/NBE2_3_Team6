package com.example.filmpass.dto

import com.example.filmpass.entity.Reservation
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReservationReadDto(
    val userId: Long?,
    val reservationDate: LocalDateTime?,
    val seatId: Long?,
    val cinemaMovieName: String?,
    val reservationId: Long?,
    val adult: Int,
    val child: Int,
    val youth: Int,
    val showTime: LocalTime?,
    val screenDateTime: LocalDate?,
    val cinemaName: String?
) {
    constructor(reservation: Reservation) : this(
        userId = reservation.member!!.memberId,
        reservationDate = reservation.bookingDate,
        seatId = reservation.seat?.seatId,
        cinemaMovieName = reservation.cinemaMovie?.movie?.movieName,
        reservationId = reservation.reserveId,
        adult = reservation.adult,
        child = reservation.child,
        youth = reservation.youth,
        showTime = reservation.cinemaMovie?.showTime,
        screenDateTime = reservation.cinemaMovie?.screenDate,
        cinemaName = reservation.cinemaMovie?.cinema?.cinemaName
    )

}