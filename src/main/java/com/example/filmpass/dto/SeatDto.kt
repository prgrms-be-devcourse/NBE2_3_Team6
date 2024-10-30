package com.example.filmpass.dto

import com.example.filmpass.entity.Cinema
import com.example.filmpass.entity.CinemaMovie
import com.example.filmpass.entity.Seat


data class SeatDto(
    val seatId: Long? = null,
    val cinemaName: String? = null,
    val seatX :Int = 0,
    val seatY:Int = 0,
    val isReserved:Boolean = false,
    val cinemaMovieId: Long? = null
){
    fun toEntity(cinema: Cinema, cinemaMovie: CinemaMovie): Seat {
        return Seat(
            seatRow = this.seatX,
            seatCol = this.seatY,
            isReserved = this.isReserved,
            cinema = cinema,
            cinemaMovie = cinemaMovie
        )
    }
}
