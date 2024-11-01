package com.example.filmpass.dto

import com.example.filmpass.entity.Cinema

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

class CinemaDto(cinema: Cinema) {
    var id: Long? = null
    var cinemaName = cinema.cinemaName

    //최대 좌석 행
    var seatRow = cinema.seatRow

    //최대 좌석 열
    var seatCol = cinema.seatCol

    fun toEntity(): Cinema {
        val cinema = Cinema(
            cinemaId = id,
            cinemaName = cinemaName,
            seatRow = seatRow,
            seatCol = seatCol)
        return cinema
    }
}
