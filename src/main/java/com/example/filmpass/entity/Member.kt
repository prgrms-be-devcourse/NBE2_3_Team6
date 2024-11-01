package com.example.filmpass.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lombok.Getter
import lombok.Setter

@Entity
class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id") //  기본 키
    var memberId: Long? = null

    @get:NotNull
    @get:Size(min = 4)
    var password: String? = null // 비밀번호

    @Column(unique = true)
    var id: String? = null // 사용자 ID

    @Column(unique = true)
    var email: String? = null // 이메일

    @Column(unique = true)
    @get:NotNull
    @get:Size(min = 10, max = 15)
    var number: String? = null // 폰번호

    var image: String? = null // 프로필 사진
    var role: String? = null // 권한

    var refreshToken: String? = null // 재생성 토큰

    @Column(name = "name")
    var name: String? = null // 사용자 이름 추가

    var point: Long? = null // 포인트 필드 추가
}
