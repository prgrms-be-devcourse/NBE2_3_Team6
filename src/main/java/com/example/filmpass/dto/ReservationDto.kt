package com.example.filmpass.dto

import com.example.filmpass.entity.CinemaMovie
import com.example.filmpass.entity.Member
import com.example.filmpass.entity.Reservation
import com.example.filmpass.entity.Seat
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime



class ReservationDto(reservation: Reservation) {
    private val reservationId: Long? = reservation.reserveId
    val userId = reservation.member?.memberId
    private val reservationDate: LocalDateTime? = reservation.bookingDate
    val seatId: Long? = reservation.seat?.seatId
    val cinemaMovieId: Long? = null

    //        this.cinemaMovieId = reservation.getCinemaMovie().getCinemaMovieId();
    private val adult = reservation.adult
    private val child = reservation.child
    private val youth = reservation.youth

    fun toEntity(seat: Seat?, cinemaMovie: CinemaMovie?, member: Member?): Reservation {
        return Reservation(
            reserveId = reservationId,
            member = member,
            seat = seat,
            cinemaMovie = cinemaMovie,
            bookingDate = LocalDateTime.now(),
            adult = adult,
            youth = youth,
            child = child)
    }
}
