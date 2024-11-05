package com.example.filmpass.repository

import com.example.filmpass.dto.ReviewDTO
import com.example.filmpass.entity.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ReviewRepository : JpaRepository<Review?, Long?> {
    @Query("SELECT r FROM Review r WHERE r.movie.movieId = :movieId")
    fun list(@Param("movieId") movieId: Long?, pageable: Pageable?): Page<Review>?
}
















