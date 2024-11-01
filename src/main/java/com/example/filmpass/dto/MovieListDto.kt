package com.example.filmpass.dto

import com.example.filmpass.entity.CinemaMovie

data class MovieListDto(
    val title: String? = null,
    val info: List<CinemaMovieDto>? = null
) {
    // 보조 생성자 제공
    constructor(id: Long, movieName: CinemaMovie, infoDto: List<CinemaMovieDto>) : this(
        title = movieName.movie?.movieName,
        info = infoDto
    )
}
