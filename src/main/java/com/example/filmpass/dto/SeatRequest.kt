package com.example.filmpass.dto

data class SeatRequest (
    val cinemaMovieId: Long? = null,
    val cinemaId: Long? = null,

    //좌석 하나하나
    val rows:Int = 0,
    val cols:Int = 0
)
