package com.example.filmpass.controller

import com.example.filmpass.dto.ReservationDto
import com.example.filmpass.dto.ReservationReadDto
import com.example.filmpass.service.ReservationService
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
@Log4j2
class ReservationController {
    private val reservationService: ReservationService? = null


    //예매 등록
    @PostMapping
    fun createReservation(@RequestBody reservationDto: ReservationDto?): ResponseEntity<ReservationDto> {
        return ResponseEntity.ok(reservationService!!.create(reservationDto!!))
    }

    //예매 조회
    @GetMapping("/{id}")
    fun read(@PathVariable id: Long?): ResponseEntity<ReservationReadDto> {
        return ResponseEntity.ok(reservationService!!.read(id!!))
    }

    @DeleteMapping("/{id}")
    fun deleteReservation(@PathVariable id: Long?): ResponseEntity<Void> {
        reservationService!!.remove(id!!)
        return ResponseEntity.noContent().build()
    }
}
