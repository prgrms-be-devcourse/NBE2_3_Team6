package com.example.filmpass.dto

import com.example.filmpass.entity.Movie
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KmdbMovieInfoResponse(
    @JsonProperty("title")
    val title: String? = null,

    @JsonProperty("plots")
    val plots: Array<Plot>? = null,

    @JsonProperty("posters")
    val posters: String? = null
) {
    data class Plot(
        @JsonProperty("plotText")
        val plotText: String? = null
    )

    // DTO를 Movie 엔티티로 변환하는 메서드
    fun toEntity(movieCd: String, movieName: String): Movie {
        return Movie(
            movieCd = movieCd,                // Kobis에서 가져온 movieCd
            movieName = movieName,            // Kobis에서 가져온 영화 제목
            plot = getPlot(),                 // KMDB에서 가져온 줄거리
            poster = getPosterUrl()           // KMDB에서 가져온 포스터 URL
        )
    }

    // 첫 번째 plotText 반환
    fun getPlot(): String? {
        return plots?.firstOrNull()?.plotText
    }

    // 첫 번째 포스터 URL 반환
    fun getPosterUrl(): String? {
        return posters?.split("|")?.firstOrNull()
    }
}
