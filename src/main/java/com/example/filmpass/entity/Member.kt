package com.example.filmpass.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lombok.Getter
import lombok.Setter

@Entity
@Getter
@Setter
class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id") //  기본 키
    private var memberId: Long? = null

    private val password: @NotNull @Size(min = 4) String? = null // 비밀번호

    @Column(unique = true)
    private var id: String? = null // 사용자 ID

    @Column(unique = true)
    private var email: String? = null // 이메일

    @Column(unique = true)
    private var number: @NotNull @Size(min = 10, max = 15) String? = null // 폰번호

    private val image: String? = null // 프로필 사진
    private val role: String? = null // 권한

    private val refreshToken: String? = null // 재생성 토큰

    @Column(name = "name")
    private var name: String? = null // 사용자 이름 추가


    @Getter
    @Setter
    private val point: Long? = null // 포인트 필드 추가
}
