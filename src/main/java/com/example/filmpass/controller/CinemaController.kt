package com.example.filmpass.controller

import com.example.filmpass.dto.CinemaDto
import com.example.filmpass.service.CinemaService
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/cinema")
@Log4j2
class CinemaController {
    private val cinemaService: CinemaService? = null

    //    {
    //        "cinemaName": "5번 상영관",
    //            "seatRow": 10,
    //            "seatCol": 10
    //
    //    }
    //  상영관 생성
    //    @PostMapping()
    //    public ResponseEntity<CinemaDto> createCinema(@RequestBody CinemaDto cinemaDto) {
    //        return ResponseEntity.ok(cinemaService.registerCinema(cinemaDto));
    //    }
    //상영관 조회
    @GetMapping
    fun read(): ResponseEntity<List<CinemaDto>> {
        return ResponseEntity.ok(cinemaService!!.read())
    }
}
