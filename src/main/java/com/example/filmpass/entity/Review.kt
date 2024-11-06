package com.example.filmpass.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "review")
@EntityListeners(
    AuditingEntityListener::class
)
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val reviewId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    val movie: Movie? = null,

    @Column(nullable = false)
    var reviewer: String? = null,

    @Column(nullable = false)
    var content: String? = null,

    @Column(nullable = false)
    var rating: Int? = 0,

    @CreatedDate
    var regDate: LocalDateTime? = null,

    @LastModifiedDate
    var modDate: LocalDateTime? = null


){
    fun changeContent(newContent: String) {
    this.content = newContent
        this.modDate = LocalDateTime.now()
}

    fun changeRating(newRating: Int) {
        this.rating = newRating
        this.modDate = LocalDateTime.now()
    }
}