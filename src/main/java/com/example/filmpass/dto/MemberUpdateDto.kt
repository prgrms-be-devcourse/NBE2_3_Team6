package com.example.filmpass.dto

import org.springframework.web.multipart.MultipartFile

data class MemberUpdateDto(
    var image: MultipartFile? // 변경할 프로필 사진 파일
)
