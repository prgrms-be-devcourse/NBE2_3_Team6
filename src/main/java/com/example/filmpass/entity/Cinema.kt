package com.example.filmpass.entity

import jakarta.persistence.*
import lombok.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "cinema")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(
    AuditingEntityListener::class
)
    class Cinema(id: Long?, cinemaName: String?, seatRow: Int, seatCol: Int) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cinemaId: Long? = null

    var cinemaName: String? = null

    var seatRow = 0

    var seatCol = 0
}
