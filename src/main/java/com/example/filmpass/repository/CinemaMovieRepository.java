package com.example.filmpass.repository;

import com.example.filmpass.dto.CinemaMovieDto;
import com.example.filmpass.entity.CinemaMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CinemaMovieRepository extends JpaRepository<CinemaMovie, Long> {
    List<CinemaMovie> findByMovie_MovieId(Long movieId);

//    Optional<CinemaMovie> findByMovieMovieIdAndCinemaCinemaIdAndScreenDateAndScreenTime(Long movieId, Long cinemaId, LocalDate screenDate, LocalTime screenTime);
}
