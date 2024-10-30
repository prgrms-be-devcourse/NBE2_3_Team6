package com.example.filmpass.dto

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class MemberLoginDto {
    private val id: String? = null // 사용자 ID
    private val password: String? = null // 비밀번호
}
