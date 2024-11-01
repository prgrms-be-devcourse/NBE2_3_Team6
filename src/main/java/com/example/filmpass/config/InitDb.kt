package com.example.filmpass.config

import com.example.filmpass.entity.Cinema
import com.example.filmpass.repository.CinemaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class InitDb (val cinemaRepository: CinemaRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (cinemaRepository.findAll().isEmpty()) {
            cinemaRepository.save(Cinema(cinemaName = "Cinema One", seatRow = 5, seatCol = 10))
            cinemaRepository.save(Cinema(cinemaName = "Cinema Two", seatRow = 5, seatCol = 10))
            cinemaRepository.save(Cinema(cinemaName = "Cinema Three", seatRow = 5, seatCol = 10))
            cinemaRepository.save(Cinema(cinemaName = "Cinema Four", seatRow = 5, seatCol = 10))
            cinemaRepository.save(Cinema(cinemaName = "Cinema Five", seatRow = 5, seatCol = 10))
        }
    }
}