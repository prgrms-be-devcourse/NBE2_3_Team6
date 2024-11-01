package com.example.filmpass.entity

import jakarta.persistence.*
import lombok.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(
    AuditingEntityListener::class
)
class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var seatId: Long? = null

    private val seatRow = 0

    private val seatCol = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    private val cinema: Cinema? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_movie_id")
    private val cinemaMovie: CinemaMovie? = null

    var isReserved = false
}
