package com.example.filmpass.service

import com.example.filmpass.dto.SeatDto
import com.example.filmpass.dto.SeatRequest
import com.example.filmpass.entity.Seat
import com.example.filmpass.repository.CinemaMovieRepository
import com.example.filmpass.repository.CinemaRepository
import com.example.filmpass.repository.SeatRepository
import jakarta.persistence.EntityNotFoundException
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SeatService (
        private val seatRepository: SeatRepository,
        private val cinemaRepository: CinemaRepository,
        private val cinemaMovieRepository: CinemaMovieRepository
) {

    fun create(seatRequest: SeatRequest): List<SeatDto> {
        val seatDtoList: MutableList<SeatDto> = mutableListOf()

        //중복 생성 방지
        if (seatRepository.existsByCinemaMovie_CinemaMovieIdAndCinema_cinemaId(
                seatRequest.cinemaMovieId,
                seatRequest.cinemaId
        )
        ) {
            return seatDtoList
        }

        val cinemaMovie = cinemaMovieRepository.findByIdOrNull(seatRequest.cinemaMovieId)
                ?: throw EntityNotFoundException("CinemaMovie Not Found")

        val cinema = cinemaRepository.findByIdOrNull(seatRequest.cinemaId)
            ?: throw EntityNotFoundException("Cinema Not Found")

        val rows: Int = cinema.seatRow
        val cols: Int = cinema.seatCol

        for (row in 1..rows) {
            for (col in 1..cols) {
                val seatDto: SeatDto = SeatDto(
                        seatX = row,
                        seatY = col,
                        isReserved = false,
                        cinemaName = cinema.cinemaName,
                        cinemaMovieId = cinemaMovie.cinemaMovieId
                )

                val seat: Seat = seatDto.toEntity(cinema, cinemaMovie)
                val savedSeat = seatRepository.save(seat)
                seatDto.seatId = savedSeat.seatId

                seatDtoList.add(seatDto)
            }
        }
        return seatDtoList
    }

    //    //좌석 조회
    fun read(cinemaMovieId: Long?): List<SeatDto>? {
        val seats: List<Seat?>? = seatRepository.findByCinemaMovieCinemaMovieId(cinemaMovieId)
        return seats?.mapNotNull { seat ->
                seat?.let {
            SeatDto(
                    seatId = it.seatId,
                    seatX = it.seatRow,
                    seatY = it.seatCol,
                    cinemaName = it.cinema?.cinemaName,
                    isReserved = it.isReserved,
                    cinemaMovieId = it.cinemaMovie?.cinemaMovieId
                )
        }
        }
    }

    //    //    좌석 선택
    fun reserveSeat(seatRequest: SeatRequest): Any = run{
        val seat: Seat? = seatRepository.findBySeatRowAndSeatColAndCinemaMovieCinemaMovieId(
                seatRequest.rows,
                seatRequest.cols,
                seatRequest.cinemaMovieId
        )
        if (seat?.isReserved == true) {
            log.warn("Reserved Seat")
            throw IllegalStateException("이미 예매된 좌석")
        }
        if (seat != null) {
            return SeatDto(
                    seatId = seat.seatId,
                    seatX = seat.seatRow,
                    seatY = seat.seatCol,
                    isReserved = seat.isReserved,
                    cinemaMovieId = seat.cinemaMovie?.cinemaMovieId
            )
        }
    }
}
