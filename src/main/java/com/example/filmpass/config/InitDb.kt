package com.example.filmpass.config

import com.example.filmpass.entity.Cinema
import com.example.filmpass.repository.CinemaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class InitDb (val cinemaRepository: CinemaRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (cinemaRepository.findAll().isEmpty()) {
            cinemaRepository.save(Cinema(cinemaName = "Cinema One", seatRow = 10, seatCol = 15))
            cinemaRepository.save(Cinema(cinemaName = "Cinema Two", seatRow = 8, seatCol = 12))
            cinemaRepository.save(Cinema(cinemaName = "Cinema Three", seatRow = 12, seatCol = 20))
        }
    }
}