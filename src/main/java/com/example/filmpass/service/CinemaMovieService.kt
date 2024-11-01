package com.example.filmpass.service

import com.example.filmpass.dto.CinemaMovieDto
import com.example.filmpass.dto.MovieListDto
import com.example.filmpass.entity.CinemaMovie
import com.example.filmpass.repository.CinemaMovieRepository
import com.example.filmpass.repository.CinemaRepository
import com.example.filmpass.repository.MovieRepository
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
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
        val moviesDto: MutableList<CinemaMovieDto> = ArrayList()

        val random = Random()

        for (movie in movies) {
            val allminutes = movie.showTm!!.toInt()
            val hour = allminutes / 60
            val min = allminutes % 60
            val showtime = LocalTime.of(hour, min)
            if (cinemaMovieRepository!!.findByMovie_MovieId(movie.movieId).isEmpty()) {
                for (i in 0..6) {
                    val cinemaId = (random.nextInt(5) + 1).toLong()

                    val cinema = cinemaRepository!!.findById(cinemaId).get()
                    val cinemaMovie = CinemaMovie(
                        movie = movie, // movie 객체 설정
                        screenDate = LocalDate.from(LocalDateTime.now().plusDays(i.toLong())) ,// 오늘부터 i일 더한 날짜로 설정
                        screenTime = showtime ,// showtime은 상영 시간으로 설정
                        showTime = generateRandomTime(),
                        cinema = cinema)


                    cinemaMovieRepository.save(cinemaMovie) // cinemaMovie 저장
                    val cinemaMovieDto = CinemaMovieDto(
                        cinemaMovie.cinemaMovieId,
                        cinemaMovie.movie,
                        cinemaMovie.screenDate,
                        cinemaMovie.screenTime,
                        cinemaMovie.showTime,
                        cinemaMovie.movie!!.movieName,
                        cinemaMovie.cinema
                    )

                    moviesDto.add(cinemaMovieDto)
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


    //    public CinemaMovieDto registerCinema(CinemaMovieDto cinemaMovieDto) {
    //        Movie movie = movieRepository.findById(cinemaMovieDto.getMovieId()).get();
    //        Cinema cinema = cinemaRepository.findById(cinemaMovieDto.getCinemaId()).get();
    //
    //        Optional<CinemaMovie> error = cinemaMovieRepository
    //                .findByMovieMovieIdAndCinemaCinemaIdAndScreenDateAndScreenTime(movie.getMovieId(), cinema.getCinemaId(), cinemaMovieDto.getScreenDate(), cinemaMovieDto.getScreenTime());
    //
    //        if (error.isPresent()) {
    //            throw new IllegalArgumentException("이미 등록된 영화의 상영정보 입니다");
    //        }
    //
    //        CinemaMovie cinemaMovie = cinemaMovieDto.toEntity(movie, cinema);
    //        CinemaMovie savedCinemaMovie = cinemaMovieRepository.save(cinemaMovie);
    //        return new CinemaMovieDto(movie.getMovieId(), savedCinemaMovie);
    //    }
    //        상영중인 영화 상영정보 조회
    fun read(movieId: Long?): MovieListDto {
        val cinemaMovieList = cinemaMovieRepository!!.findByMovie_MovieId(movieId)

        val infoDto: MutableList<CinemaMovieDto> = ArrayList()
        for (cinemaMovie in cinemaMovieList) {
            infoDto.add(CinemaMovieDto(movieId, cinemaMovie))
        }

        check(!cinemaMovieList.isEmpty()) { "상영정보가 없습니다" }
        val movieName = cinemaMovieList[0]

        return MovieListDto(movieId, movieName, infoDto)
    }
}