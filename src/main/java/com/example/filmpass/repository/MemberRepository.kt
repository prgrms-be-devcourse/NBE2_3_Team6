package com.example.filmpass.repository

import com.example.filmpass.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
    fun findById(id: String): Optional<Member> // 회원 ID로 조회하는 메소드
    fun findByNumber(number: String): Optional<Member>
}
