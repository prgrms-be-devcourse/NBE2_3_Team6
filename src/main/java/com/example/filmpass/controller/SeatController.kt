package com.example.filmpass.controller

import com.example.filmpass.dto.SeatDto
import com.example.filmpass.dto.SeatRequest
import com.example.filmpass.repository.SeatRepository
import com.example.filmpass.service.SeatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/seat")
class SeatController {
    private val seatService: SeatService? = null
    private val seatRepository: SeatRepository? = null

    //좌석 생성
    //    {
    //        "cinemaId": 1
    //    }
    @PostMapping
    fun create(@RequestBody seatRequest: SeatRequest) = run{
        ResponseEntity.ok(seatService?.create(seatRequest))
    }

//    //좌석 조회
//    @GetMapping("/{cinemaMovieId}")
//    fun read(@PathVariable cinemaMovieId: Long?): List<SeatDto> {
//        return seatService.read(cinemaMovieId)
//    }
//
//    //좌석 선택
//    //    {
//    //        "cinemaMovieId":2,
//    //            "rows":3,
//    //            "cols":7
//    //    }
//    @PutMapping("/choice")
//    fun update(@RequestBody seatRequest: SeatRequest?): ResponseEntity<SeatDto> {
//        return ResponseEntity.ok(seatService.reserveSeat(seatRequest))
//    } //    @PutMapping("/cancel")
//    //    public ResponseEntity<Seat> cancel(@RequestBody SeatRequest seatRequest) {
//    //        Seat seat = seatRepository.findById(seatRequest.getCinemaMovieId()).orElseThrow();
//    //        seat.setReserved(false);
//    //        return ResponseEntity.ok(seatRepository.save(seat));
//    //    }
}
