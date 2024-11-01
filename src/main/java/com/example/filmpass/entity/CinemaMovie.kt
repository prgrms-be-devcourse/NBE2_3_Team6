package com.example.filmpass.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "cinema_movie")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(
    AuditingEntityListener::class
)
class CinemaMovie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_movie_id")
    val cinemaMovieId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    var movie: Movie? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    var cinema: Cinema? = null,

    @OneToMany(mappedBy = "cinemaMovie")
    val seat: List<Seat> = ArrayList(),

    var screenDate: LocalDate? = null,

    var screenTime: LocalTime? = null,

    var showTime: LocalTime? = null // showTime 필드 추가
)
