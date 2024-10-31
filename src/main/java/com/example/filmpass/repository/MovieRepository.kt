package com.example.filmpass.repository

import com.example.filmpass.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface MovieRepository : JpaRepository<Movie, Long> {

    fun existsByMovieCd(movieCd: String): Boolean
    fun findByMovieName(movieName: String): Optional<Movie>
    fun findByMovieCd(movieCd: String): Movie?

    // 줄거리 및 포스터 URL 업데이트를 위한 메소드
    @Transactional
    @Modifying
    @Query("UPDATE Movie m SET m.plot = :plot, m.poster = :posterUrl WHERE m.movieCd = :movieCd")
    fun updateMoviePlotAndPoster(movieCd: String, plot: String, posterUrl: String)
}
