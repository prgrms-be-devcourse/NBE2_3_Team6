package com.example.filmpass.repository

import com.example.filmpass.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SeatRepository : JpaRepository<Seat?, Long?> {
    fun findByCinemaMovieCinemaMovieId(cinemaMovieId: Long?): List<Seat?>?

    fun findBySeatRowAndSeatColAndCinemaMovieCinemaMovieId(rows: Int, cols: Int, cinemaMovieId: Long?): Seat?

    fun existsByCinemaMovieCinemaMovieIdAndCinemaCinemaId(cinemaMovieId: Long?, cinemaId: Long?): Boolean?
}
