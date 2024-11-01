package com.example.filmpass.entity

import jakarta.persistence.*
import lombok.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "reservation")
@EntityListeners(AuditingEntityListener::class)
data class Reservation (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var reserveId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_movie_id")
    val cinemaMovie: CinemaMovie? = null,

    @CreatedDate
    val bookingDate: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    val seat: Seat? = null,

    var adult: Int = 0,

    var child: Int = 0,

    var youth: Int = 0
)
