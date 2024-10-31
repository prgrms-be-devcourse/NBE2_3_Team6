package com.example.filmpass.dto

data class DailyBoxOfficeDto(
    val rnum: String? = null,
    val rank: String? = null,
    val rankInten: String? = null,
    val rankOldAndNew: String? = null,
    val movieCd: String? = null,
    val movieNm: String? = null,
    val openDt: String? = null,
    val salesAmt: String? = null,
    val salesShare: String? = null,
    val salesInten: String? = null,
    val salesChange: String? = null,
    val salesAcc: String? = null,
    val audiCnt: String? = null,
    val audiInten: String? = null,
    val audiChange: String? = null,
    val audiAcc: String? = null,
    val scrnCnt: String? = null,
    val showCnt: String? = null
)
