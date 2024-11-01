package com.example.filmpass.dto

import com.example.filmpass.entity.Movie
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.extern.slf4j.Slf4j

@Slf4j

class MovieInfoResponse {
    private val movieInfoResult: MovieInfoResult? = null
    private val kmdbInfo: MovieInfoResponseKMDB? = null

    class MovieInfoResult {
        val movieInfo: MovieInfo? = null
    }

    class MovieInfo {
        val movieCd: String? = null
        val movieNm: String? = null
        val movieNmEn: String? = null
        val movieNmOg: String? = null
        val showTm: String? = null
        val openDt: String? = null
        val prdtStatNm: String? = null
        val typeNm: String? = null

        val nations: List<Nation>? = null
        val genres: List<Genre>? = null
        val directors: List<Director>? = null
        val actors: List<Actor>? = null
        val showTypes: List<ShowType>? = null
        val companys: List<Company>? = null
        val audits: List<Audit>? = null
        val staffs: List<Staff>? = null
    }

    class Nation {
        val nationNm: String? = null
    }

    class Genre {
        val genreNm: String? = null
    }

    class Director {
        val peopleNm: String? = null
        val peopleNmEn: String? = null
    }

    class Actor {
        val peopleNm: String? = null
        val peopleNmEn: String? = null
        val cast: String? = null
        val castEn: String? = null
    }

    class ShowType {
        val showTypeGroupNm: String? = null
        val showTypeNm: String? = null
    }

    class Company {
        val companyCd: String? = null
        val companyNm: String? = null
        val companyNmEn: String? = null
        val companyPartNm: String? = null
    }

    class Audit {
        val auditNo: String? = null
        val watchGradeNm: String? = null
    }

    class Staff {
        val peopleNm: String? = null
        val peopleNmEn: String? = null
        val staffRoleNm: String? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 필드 무시
    class MovieInfoResponseKMDB {
        val poster: String? = null
        val plot: String? = null
    }

    fun toEntity(): Movie {
        val directorName: String? = movieInfoResult?.movieInfo?.directors
            ?.firstOrNull()
            ?.peopleNm
        return Movie(
            movieCd = movieInfoResult?.movieInfo?.movieCd ?: "",
            movieName = movieInfoResult?.movieInfo?.movieNm ?: "",
            movieNameEN = movieInfoResult?.movieInfo?.movieNmEn ?: "",
            showTm = movieInfoResult?.movieInfo?.showTm ?: "",
            openDt = movieInfoResult?.movieInfo?.openDt ?: "",
            poster = kmdbInfo?.poster ?: "",
            plot = kmdbInfo?.plot ?: "",
            ageRating = movieInfoResult?.movieInfo?.audits?.firstOrNull()?.watchGradeNm ?: "",
            directorName = directorName
        )
    }
}