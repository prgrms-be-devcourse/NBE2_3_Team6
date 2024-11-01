package com.example.filmpass.dto

data class MemberSignupDto(
    val id: String?, // 사용자 ID
    val password: String?, // 비밀번호
    val email: String?, // 이메일
    val number: String?, // 전화번호
    val image: String?, // 프로필 사진
    val role: String? // 권한
)
