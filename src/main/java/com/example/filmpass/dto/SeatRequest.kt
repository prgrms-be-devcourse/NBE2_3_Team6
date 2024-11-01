package com.example.filmpass.dto

data class SeatRequest (
        val cinemaMovieId: Long? = null,
        val cinemaId: Long? = null,
        //좌석 하나하나
        var rows: Int = 0,
        var cols: Int = 0
)
