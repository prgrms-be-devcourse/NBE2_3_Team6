package com.example.filmpass.controller

import com.example.filmpass.dto.DailyBoxOfficeDto
import com.example.filmpass.entity.Movie
import com.example.filmpass.repository.MovieRepository
import com.example.filmpass.service.MovieService
import org.hibernate.query.sqm.tree.SqmNode.log
import org.slf4j.Logger
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class MovieController(
    private val movieService: MovieService,
    private val movieRepository: MovieRepository
) {
    private val apiKey = "236d4a6e256fa76f35804ceacdf28c39"

    @GetMapping("/dailyBoxOffice")
    fun getDailyBoxOffice(): List<DailyBoxOfficeDto> {
        val targetDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return movieService.updateDailyBoxOfficeWithDetails(apiKey, targetDate)
    }

    // 특정 영화 코드로 영화 정보를 가져오는 메서드
    @GetMapping("/dailyBoxOffice/{movieCd}")
    fun getMovieInfo(@PathVariable("movieCd") movieCd: String): Movie? {
        return movieService.getMovieInfo(movieCd)
    }
}
