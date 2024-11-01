package com.example.filmpass.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "cinema_movie")
@EntityListeners(
    AuditingEntityListener::class
)
data class CinemaMovie (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_movie_id")
    var cinemaMovieId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    val movie: Movie? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    val cinema: Cinema? = null,

    @OneToMany(mappedBy = "cinemaMovie")
    val seat: List<Seat> = ArrayList(),

    val screenDate: LocalDate? = null,

    val screenTime: LocalTime? = null,

    val showTime: LocalTime? = null
)
