package com.example.filmpass.dto

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class MemberSignupDto {
    private val id: String? = null // 사용자 ID
    private val password: String? = null // 비밀번호
    private val email: String? = null // 이메일
    private val number: String? = null // 전화번호
    private val image: String? = null // 프로필 사진
    private val role: String? = null // 권한
}
