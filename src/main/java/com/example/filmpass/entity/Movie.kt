package com.example.filmpass.entity

import jakarta.persistence.*
import lombok.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "movie")
@EntityListeners(
    AuditingEntityListener::class
)
data class Movie (
    //영화 상세 정보 API에 있는 내용들과 비교해서 수정하기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val movieId: Long? = null,

    val movieCd: String? = null,

    val movieName: String? = null,

    val movieNameEN: String? = null,

    val directorName: String? = null,

    val ageRating: String? = null,//관람등급

    val movieRating: Double = 0.0,

    val showTm: String? = null, //러닝타임

    val openDt: String? = null, //개봉년도

    @Lob // 이 어노테이션을 추가하여 plot을 TEXT로 처리
    @Column(columnDefinition = "TEXT") // 명시적으로 TEXT 타입 설정
    var plot: String? = null, //줄거리

    @Lob // 이 어노테이션을 추가하여 plot을 TEXT로 처리
    @Column(columnDefinition = "TEXT")
    var poster: String? = null
)