package com.example.filmpass.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "movie")
@EntityListeners(AuditingEntityListener::class)
class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val movieId: Long? = null,

    val movieCd: String? = null,

    val movieName: String? = null,

    val movieNameEN: String? = null,

    val directorName: String? = null,

    val ageRating: String? = null,  // 관람 등급

    val movieRating: Double = 0.0,

    val showTm: String? = null,     // 러닝 타임

    val openDt: String? = null,     // 개봉 연도

    @Lob
    @Column(columnDefinition = "TEXT")
    var plot: String? = null,       // 줄거리

    @Lob
    @Column(columnDefinition = "TEXT")
    var poster: String? = null      // 포스터 이미지 URL
) {
    // 기본 생성자 추가
    constructor() : this(
        movieId = null,
        movieCd = null,
        movieName = null,
        movieNameEN = null,
        directorName = null,
        ageRating = null,
        movieRating = 0.0,
        showTm = null,
        openDt = null,
        plot = null,
        poster = null
    )
}
