package com.example.filmpass.dto

data class MovieResponseDTO(
    val boxOfficeResult: BoxOfficeResult? = null
) {
    data class BoxOfficeResult(
        val boxOfficeType: String? = null,
        val showRange: String? = null,
        val dailyBoxOfficeList: List<DailyBoxOfficeDto>? = null
    )
}
