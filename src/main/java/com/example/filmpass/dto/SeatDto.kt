package com.example.filmpass.dto

import com.example.filmpass.entity.Cinema
import com.example.filmpass.entity.CinemaMovie
import com.example.filmpass.entity.Seat


data class SeatDto(
    var seatId: Long? = null,
    var cinemaName: String? = null,
    var seatX :Int = 0,
    var seatY:Int = 0,
    var isReserved:Boolean = false,
    var cinemaMovieId: Long? = null
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
