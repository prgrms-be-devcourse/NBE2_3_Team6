package com.example.filmpass.entity

import jakarta.persistence.*
import lombok.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "cinema")
@EntityListeners(
    AuditingEntityListener::class
)
data class Cinema (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cinemaId: Long? = null,

    val cinemaName: String? = null,

    val seatRow: Int = 0,

    val seatCol: Int = 0
)
