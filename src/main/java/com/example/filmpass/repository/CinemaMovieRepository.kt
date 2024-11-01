package com.example.filmpass.repository

import com.example.filmpass.entity.CinemaMovie
import org.springframework.data.jpa.repository.JpaRepository

interface CinemaMovieRepository : JpaRepository<CinemaMovie?, Long?> {
    fun findByMovie_MovieId(movieId: Long?): List<CinemaMovie?>? //    Optional<CinemaMovie> findByMovieMovieIdAndCinemaCinemaIdAndScreenDateAndScreenTime(Long movieId, Long cinemaId, LocalDate screenDate, LocalTime screenTime);
}
