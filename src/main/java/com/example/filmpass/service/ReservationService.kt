package com.example.filmpass.service

import com.example.filmpass.dto.ReservationDto
import com.example.filmpass.dto.ReservationReadDto
import com.example.filmpass.repository.CinemaMovieRepository
import com.example.filmpass.repository.MemberRepository
import com.example.filmpass.repository.ReservationRepository
import com.example.filmpass.repository.SeatRepository
import jakarta.persistence.EntityNotFoundException
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReservationService (
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository,
    private val cinemaMovieRepository: CinemaMovieRepository,
    private val memberRepository: MemberRepository){

    //예매 등록
    fun create(reservationDto: ReservationDto): ReservationDto {

        val seat = seatRepository.findById(reservationDto.seatId)
            .orElseThrow<EntityNotFoundException> { EntityNotFoundException("해당 좌석이 없습니다") }

        val cinemaMovie = cinemaMovieRepository.findById(reservationDto.cinemaMovieId)
            .orElseThrow<EntityNotFoundException> { EntityNotFoundException("해당 영화가 없습니다") }

        val member = memberRepository.findById(reservationDto.userId)
            .orElseThrow<EntityNotFoundException> { EntityNotFoundException("해당 회원이 없습니다") }

        val error = reservationRepository.findBySeatSeatId(seat.seatId)

        require(!error!!.isPresent) { "이미 등록된 좌석입니다" }

        if (!seat.isReserved) {
            seat.isReserved = true
            seatRepository.save(seat)
        } else {
            throw IllegalStateException("이미 예매된 좌석")
        }


        val reservation = reservationDto.toEntity(seat, cinemaMovie, member)
        reservationRepository.save(reservation)

        return ReservationDto(reservation)
    }

    //예매 조회
    fun read(reservationId: Long): ReservationReadDto {
        val reservation = reservationRepository.findById(reservationId).orElseThrow()!!
        return ReservationReadDto(reservation)
    }

    fun remove(reservationId: Long) {
        val reservation = reservationRepository.findById(reservationId).orElseThrow()!!

        val seat = seatRepository.findById(reservation.seat!!.seatId)
            .orElseThrow { EntityNotFoundException("해당 좌석이 없습니다") }
        seat.isReserved = false
        seatRepository.save(seat)

        reservationRepository.delete(reservation)
    }
}
