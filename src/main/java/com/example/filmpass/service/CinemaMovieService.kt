package com.example.filmpass.service

import com.example.filmpass.dto.CinemaMovieDto
import com.example.filmpass.dto.MovieListDto
import com.example.filmpass.entity.CinemaMovie
import com.example.filmpass.repository.CinemaMovieRepository
import com.example.filmpass.repository.CinemaRepository
import com.example.filmpass.repository.MovieRepository
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
class CinemaMovieService {
    private val cinemaMovieRepository: CinemaMovieRepository? = null
    private val cinemaRepository: CinemaRepository? = null
    private val movieRepository: MovieRepository? = null

    fun registerCinema(): List<CinemaMovieDto> {
        val movies = movieRepository!!.findAll()
        var moviesDto: MutableList<CinemaMovieDto> = ArrayList()
        val random = Random()

        for (movie in movies) {
            // movie.showTm이 null인지 체크
            val allminutes = movie.showTm?.toInt() ?: continue // showTm이 null일 경우, 다음 movie로 넘어감
            val hour = allminutes / 60
            val min = allminutes % 60
            val showtime = LocalTime.of(hour, min)

            // cinemaMovieRepository에서 null 체크
            if (cinemaMovieRepository?.findByMovie_MovieId(movie.movieId)?.isEmpty() == true) {
                for (i in 0..6) {
                    val cinemaId = (random.nextInt(5) + 1).toLong()

                    // cinemaId에 해당하는 cinema 객체가 있는지 체크
                    val cinemaOptional = cinemaRepository?.findById(cinemaId)
                    if (cinemaOptional?.isPresent == true) {
                        val cinema = cinemaOptional.get()

                        val cinemaMovie: CinemaMovie = CinemaMovie(
                            movie = movie, // movie 객체 설정
                            screenDate = LocalDate.now().plusDays(i.toLong()), // 오늘부터 i일 더한 날짜로 설정
                            screenTime = showtime, // showtime은 상영 시간으로 설정
                            showTime = generateRandomTime(), // 무작위 시간 생성
                            cinema = cinema // cinema 객체 설정
                        )

                        // cinemaMovie 저장
                        cinemaMovieRepository?.save(cinemaMovie)

                        val cinemaMovieDto = CinemaMovieDto(
                            cinemaMovie.cinemaMovieId,
                            cinemaMovie.movie,
                            cinemaMovie.screenDate,
                            cinemaMovie.screenTime,
                            cinemaMovie.showTime,
                            cinemaMovie.movie?.movieName ?: "Unknown", // movieName null 체크
                            cinemaMovie.cinema
                        )

                        moviesDto.add(cinemaMovieDto)
                    } else {
                        log.warn("Cinema with ID $cinemaId not found.")
                    }
                }
            }
        }
        return moviesDto
    }
    private fun generateRandomTime(): LocalTime {
        val random = Random()
        val hour = random.nextInt(17) + 7
        val min = random.nextInt(60)
        return LocalTime.of(hour, min)
    }

    fun read(movieId: Long?): MovieListDto? {
        val cinemaMovieList = cinemaMovieRepository!!.findByMovie_MovieId(movieId)

        val infoDto: MutableList<CinemaMovieDto> = ArrayList()
        if (cinemaMovieList != null) {
            for (cinemaMovie in cinemaMovieList) {
                cinemaMovie?.let { CinemaMovieDto(movieId, it) }?.let { infoDto.add(it) }
            }
        }

        if (cinemaMovieList != null) {
            check(cinemaMovieList.isNotEmpty()) { "상영정보가 없습니다" }
        }
        val movieName = cinemaMovieList?.get(0)

        return movieName?.let { MovieListDto(movieId!!, it, infoDto) }
    }
}