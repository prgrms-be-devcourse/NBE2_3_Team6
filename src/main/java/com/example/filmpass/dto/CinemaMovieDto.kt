package com.example.filmpass.dto

import com.example.filmpass.entity.Cinema
import com.example.filmpass.entity.CinemaMovie
import com.example.filmpass.entity.Movie
import com.fasterxml.jackson.annotation.JsonInclude
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDate
import java.time.LocalTime
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CinemaMovieDto (
    val movieId: Long? = null,

    var cinemaId: Long? = null,

    var cinemaMovieId: Long?,

    var title: String?,

    var cinemaName: String? = null,

    var screenDate: LocalDate?,

    var screenTime: LocalTime?,

    var showTime: LocalTime?,

    var movie: Movie? = null,

    var cinema: Cinema? = null
){
    constructor(id: Long?, cinemaMovie: CinemaMovie) : this(
        movieId = cinemaMovie.movie?.movieId,
        cinemaId = cinemaMovie.cinema?.cinemaId,
        cinemaMovieId = cinemaMovie.cinemaMovieId,
        title = cinemaMovie.movie?.movieName,
        cinemaName = cinemaMovie.cinema?.cinemaName,
        screenDate = cinemaMovie.screenDate,
        screenTime = cinemaMovie.screenTime,
        showTime = cinemaMovie.showTime,
        movie = cinemaMovie.movie,
        cinema = cinemaMovie.cinema
    )


    constructor(
        id: Long?,
        movie: Movie?,
        screenDate: LocalDate?,
        screenTime: LocalTime?,
        showTime: LocalTime?,
        title: String?,
        cinema: Cinema?
    ) : this(
        movieId = movie?.movieId,
        cinemaId = cinema?.cinemaId,
        cinemaMovieId = id,
        title = title,
        cinemaName = cinema?.cinemaName,
        screenDate = screenDate,
        screenTime = screenTime,
        showTime = showTime,
        movie = movie,
        cinema = cinema
    )

    fun toEntity(movie: Movie?, cinema: Cinema?): CinemaMovie {
        return CinemaMovie(
            movie = movie,
            cinema = cinema,
            screenDate = screenDate,
            screenTime = screenTime,
            showTime = showTime
        )
    }
}
