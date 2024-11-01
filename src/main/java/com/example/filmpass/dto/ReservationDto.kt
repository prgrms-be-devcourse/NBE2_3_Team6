package com.example.filmpass.dto

import com.example.filmpass.entity.CinemaMovie
import com.example.filmpass.entity.Member
import com.example.filmpass.entity.Reservation
import com.example.filmpass.entity.Seat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReservationDto(
    var reservationId: Long? = null,
    var userId: Long? = null,
    var reservationDate: LocalDateTime? = null,
    var seatId: Long? = null,
    var cinemaMovieId: Long? = null,
    var adult: Int = 0,
    var child: Int = 0,
    var youth: Int = 0
) {
    constructor(reservation: Reservation) : this(
        reservationId = reservation.reserveId,
        userId = reservation.member?.memberId,
        reservationDate = reservation.bookingDate,
        seatId = reservation.seat?.seatId,
        adult = reservation.adult,
        child = reservation.child,
        youth = reservation.youth
    )

    fun toEntity(seat: Seat?, cinemaMovie: CinemaMovie?, member: Member?): Reservation {
        return Reservation(
            reserveId = reservationId,
            member = member,
            seat = seat,
            cinemaMovie = cinemaMovie,
            bookingDate = LocalDateTime.now(),
            adult = adult,
            youth = youth,
            child = child
        )
    }
}
