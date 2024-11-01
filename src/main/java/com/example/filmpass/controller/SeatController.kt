package com.example.filmpass.controller

import com.example.filmpass.dto.SeatDto
import com.example.filmpass.dto.SeatRequest
import com.example.filmpass.repository.SeatRepository
import com.example.filmpass.service.SeatService
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/seat")
@Log4j2
class SeatController(private val seatService: SeatService) {


    //좌석 생성
    @PostMapping
    fun create(@RequestBody seatRequest: SeatRequest): ResponseEntity<List<SeatDto>> {
        val createdSeats = seatService?.create(seatRequest)
        return ResponseEntity.ok(createdSeats)
    }

    //좌석 조회
    @GetMapping("/{cinemaMovieId}")
    fun read(@PathVariable cinemaMovieId: Long): List<SeatDto>? {
        return seatService.read(cinemaMovieId)
    }

    //좌석 선택
    //    {
    //        "cinemaMovieId":2,
    //            "rows":3,
    //            "cols":7
    //    }
    @PutMapping("/choice")
    fun update(@RequestBody seatRequest: SeatRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(seatService.reserveSeat(seatRequest))
    }
}
