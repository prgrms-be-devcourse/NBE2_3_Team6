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
        if (seatRepository.existsByCinemaMovieCinemaMovieIdAndCinemaCinemaId(
                seatRequest.cinemaMovieId,
                seatRequest.cinemaId
            ) == true
        ) {
            return seatDtoList
        }

        val cinemaMovie = cinemaMovieRepository.findByIdOrNull(seatRequest.cinemaMovieId)
            ?: throw EntityNotFoundException("CinemaMovie Not Found")

        val cinema = cinemaMovie.cinema

        val rows: Int? = cinema?.seatRow
        val cols: Int? = cinema?.seatCol

        for (row in 1..rows!!) {
            for (col in 1..cols!!) {
                val seatDto: SeatDto = SeatDto(
                    seatX = row,
                    seatY = col,
                    isReserved = false,
                    cinemaName = cinema.cinemaName,
                    cinemaMovieId = cinemaMovie.cinemaMovieId
                )

                val seat: Seat = seatDto.toEntity(cinema, cinemaMovie)
                seatRepository.save<Seat>(seat)

                seatDto.seatId = seat.seatId
                seatDtoList.add(seatDto)
            }
        }
        return seatDtoList
    }

//    //좌석 조회
//    fun read(cinemaMovieId: Long?): List<SeatDto> {
//        val seats: List<Seat> = seatRepository.findByCinemaMovieCinemaMovieId(cinemaMovieId)
//        val seatDtoList: MutableList<SeatDto> = ArrayList<SeatDto>()
//
//        for (seat in seats) {
//            val seatDto: SeatDto = SeatDto()
//            seatDto.setSeatId(seat.seatId)
//            seatDto.setSeatX(seat.seatRow)
//            seatDto.setSeatY(seat.seatCol)
//            seatDto.setCinemaName(seat.cinema.cinemaName)
//            seatDto.setReserved(seat.isReserved)
//            seatDto.setCinemaMovieId(seat.cinemaMovie.getCinemaMovieId())
//
//            seatDtoList.add(seatDto)
//        }
//        return seatDtoList
//    }
//
//    //    좌석 선택
//    fun reserveSeat(seatRequest: SeatRequest): SeatDto {
//        val seat: Seat = seatRepository.findBySeatRowAndSeatColAndCinemaMovieCinemaMovieId(
//            seatRequest.rows,
//            seatRequest.cols,
//            seatRequest.cinemaMovieId
//        )
//        if (!seat.isReserved) {
//            seat.setReserved(false)
//            //            seatRepository.save(seat);
//        } else {
//            log.warn("Reserved Seat")
//            throw IllegalStateException("이미 예매된 좌석")
//        }
//        return SeatDto.builder()
//            .seatId(seat.seatId)
//            .seatX(seat.seatRow)
//            .seatY(seat.seatCol)
//            .isReserved(seat.isReserved)
//            .build()
//    }
}