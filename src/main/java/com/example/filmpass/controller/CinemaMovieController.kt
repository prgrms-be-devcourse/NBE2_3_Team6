package com.example.filmpass.controller

import com.example.filmpass.dto.CinemaMovieDto
import com.example.filmpass.dto.MovieListDto
import com.example.filmpass.service.CinemaMovieService
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/cinemaMovie")
@Log4j2
class CinemaMovieController (private val cinemaMovieService: CinemaMovieService){


    //상영중인 영화 등록
    @PostMapping //    public ResponseEntity<CinemaMovieDto> create(@RequestBody CinemaMovieDto cinemaMovieDto) {
    //        return ResponseEntity.ok(cinemaMovieService.registerCinema(cinemaMovieDto));
    fun create(): ResponseEntity<List<CinemaMovieDto>> {
        return ResponseEntity.ok(cinemaMovieService.registerCinema())
    }

    //상영중인 영화 조회
    @GetMapping("/{movieId}")
    fun read(@PathVariable movieId: Long?): ResponseEntity<MovieListDto> {
        return ResponseEntity.ok(cinemaMovieService.read(movieId))
    }
}
