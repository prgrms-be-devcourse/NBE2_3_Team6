package com.example.filmpass.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "seat")
@EntityListeners(AuditingEntityListener::class)
class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var seatId: Long? = null

    var seatRow: Int = 0

    var seatCol: Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    var cinema: Cinema? = null // private -> public으로 변경

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_movie_id")
    var cinemaMovie: CinemaMovie? = null // private -> public으로 변경

    var isReserved: Boolean = false // private -> public으로 변경 및 기본값 설정
}
