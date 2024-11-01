package com.example.filmpass.entity;

import jakarta.persistence.*

@Entity
@Table(name = "seat")
data class Seat(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val seatId: Long? = null,
        var seatRow:Int,
        var seatCol:Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cinema_id")
        var cinema: Cinema? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cinema_movie_id")
        var cinemaMovie: CinemaMovie? = null,

        var isReserved : Boolean=false
)
