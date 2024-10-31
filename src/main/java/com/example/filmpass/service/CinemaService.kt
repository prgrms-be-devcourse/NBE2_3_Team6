package com.example.filmpass.service

import com.example.filmpass.dto.CinemaDto
import com.example.filmpass.repository.CinemaRepository
import com.example.filmpass.repository.SeatRepository
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
class CinemaService {
    private val cinemaRepository: CinemaRepository? = null
    private val seatRepository: SeatRepository? = null

    //상영관 등록
//    fun registerCinema(cinemaDto: CinemaDto): CinemaDto {
//        val cinema = cinemaDto.toEntity()
//        cinemaRepository!!.save(cinema)
//        return CinemaDto(cinema)
//    }

    //상영관 조회
    fun read(): List<CinemaDto> {
        val cinemas = cinemaRepository!!.findAll()
        val cinemaDtoList: MutableList<CinemaDto> = ArrayList()

        for (cinema in cinemas) {
            val cinemaDto = CinemaDto()
            cinemaDto.id = cinema!!.cinemaId
            cinemaDto.cinemaName = cinema.cinemaName
            cinemaDto.seatRow = cinema.seatRow
            cinemaDto.seatCol = cinema.seatCol

            cinemaDtoList.add(cinemaDto)
        }
        return cinemaDtoList
    }
}
