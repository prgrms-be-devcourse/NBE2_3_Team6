package com.example.filmpass.dto

import com.example.filmpass.entity.Cinema
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

data class CinemaDto(
    var id: Long? = null,
    var cinemaName: String? = null,
    var seatRow: Int = 0,
    var seatCol: Int = 0
)

