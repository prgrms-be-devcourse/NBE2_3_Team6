package com.example.filmpass.dto

import com.example.filmpass.entity.Cinema
import com.example.filmpass.entity.CinemaMovie

import com.example.filmpass.entity.Movie
import com.fasterxml.jackson.annotation.JsonInclude
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDate
import java.time.LocalTime

@Data
@NoArgsConstructor
@AllArgsConstructor
class CinemaMovieDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private val movieId: Long? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private var cinemaId: Long? = null

    private var cinemaMovieId: Long?

    private var title: String?

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private var cinemaName: String? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private var screenDate: LocalDate?

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private var screenTime: LocalTime?

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private var showTime: LocalTime?

    private var movie: Movie? = null

    private var cinema: Cinema? = null

    constructor(
        id: Long?,
        movie: Movie?,
        screenDate: LocalDate?,
        screenTime: LocalTime?,
        showTime: LocalTime?,
        title: String?,
        cinema: Cinema?
    ) {
        this.cinemaMovieId = id
        this.movie = movie
        this.screenDate = screenDate
        this.screenTime = screenTime
        this.showTime = showTime
        this.title = title
        this.cinema = cinema
    }


    constructor(id: Long?, cinemaMovie: CinemaMovie) {
        this.cinemaId = cinemaMovie.cinema!!.cinemaId
        this.title = cinemaMovie.movie!!.movieName
        this.cinemaMovieId = cinemaMovie.cinemaMovieId
        this.cinemaName = cinemaMovie.cinema.cinemaName
        this.screenDate = cinemaMovie.screenDate
        this.screenTime = cinemaMovie.screenTime
        this.showTime = cinemaMovie.showTime
    }

    fun toEntity(movie: Movie?, cinema: Cinema?): CinemaMovie {
        return CinemaMovie(
            movie = movie,
            cinema = cinema,
            screenDate = screenDate,
            screenTime = screenTime)
    }
}
