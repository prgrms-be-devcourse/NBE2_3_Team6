package com.example.filmpass.controller

import com.example.filmpass.dto.DailyBoxOfficeDto
import com.example.filmpass.entity.Movie
import com.example.filmpass.repository.MovieRepository
import com.example.filmpass.service.MovieService
import org.slf4j.Logger
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class MovieController(
    private val movieService: MovieService,
    private val movieRepository: MovieRepository,
    private val log: Logger
) {
    private val apiKey = "236d4a6e256fa76f35804ceacdf28c39"

    @GetMapping("/dailyBoxOffice")
    fun getDailyBoxOffice(): List<DailyBoxOfficeDto> {
        // 어제 날짜를 yyyyMMdd 형식으로 포맷팅
        val targetDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        // MovieService를 호출하여 일일 박스오피스 정보를 가져옴
        val dailyBoxOfficeList = movieService.getDailyBoxOffice(apiKey, targetDate)

        // 1순위부터 10순위까지의 영화를 가져와 영화 정보를 DB에 저장하고 포스터와 줄거리 업데이트
        dailyBoxOfficeList.forEach { dailyBoxOfficeDto ->
            dailyBoxOfficeDto.movieCd?.let { movieCd ->
                // 영화가 DB에 있는지 확인
                if (!movieRepository.existsByMovieCd(movieCd)) {
                    log.info("Movie with movieCd $movieCd does not exist in DB, fetching details.")

                    // KOBIS API를 사용하여 영화 상세 정보를 가져옴
                    val movie = movieService.getMovieInfo(movieCd)

                    // 영화 정보가 null이 아닐 때만 저장
                    movie?.let { movie ->
                        movieRepository.save(movie)
                        log.info("Saved movie to DB: $movie")

                        // KMDB에서 포스터와 줄거리 가져와 업데이트
                        movie.directorName?.let { directorName ->
                            movieService.updateMovieWithPosterAndPlot(movie, directorName)
                        } ?: log.warn("WARN: Director name is null for movieCd $movieCd")
                    }
                } else {
                    log.warn("Movie with movieCd $movieCd already exists in DB")
                }
            }
        }

        log.info("daily box office movies updated with poster and plot")
        // 응답 반환
        return dailyBoxOfficeList
    }

    // 특정 영화 코드로 영화 정보를 가져오는 메서드
    @GetMapping("/dailyBoxOffice/{movieCd}")
    fun getMovieInfo(@PathVariable("movieCd") movieCd: String): Movie? {
        return movieService.getMovieInfo(movieCd)
    }
}
