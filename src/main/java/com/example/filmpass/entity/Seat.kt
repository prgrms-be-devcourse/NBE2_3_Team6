package com.example.filmpass.entity;

import jakarta.persistence.*

@Entity
@Table(name = "seat")
data class Seat(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val seatId: Long,
        val seatRow:Int,
        val seatCol:Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cinema_id")
        val cinema: Cinema? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cinema_movie_id")
        val cinemaMovie: CinemaMovie? = null,

        val isReserved : Boolean=false
)

