package com.example.filmpass.dto

import com.example.filmpass.entity.*
import java.time.LocalDateTime
import java.util.*

data class ReviewDTO(
    val reviewId: Long? = null,
    var content: String? = null,
    var reviewer: String? = null,
    var rating: Int? = null,
    var regDate: LocalDateTime? = null,
    var modDate: LocalDateTime? = null,
    var movieId: Long? = null,
){
    fun toEntity(movie: Movie): Review {
        return Review(
            reviewId = this.reviewId,
            content = this.content,
            reviewer = this.reviewer,
            rating = this.rating,
            movie = movie,
            regDate = this.regDate ?: LocalDateTime.now(),
            modDate = this.modDate ?: LocalDateTime.now()
        )

    }

}