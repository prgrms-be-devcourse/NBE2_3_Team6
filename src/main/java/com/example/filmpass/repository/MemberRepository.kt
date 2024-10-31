package com.example.filmpass.repository

import com.example.filmpass.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
    fun findById(id : String): Optional<Member>
    fun findByNumber(number: String): Optional<Member>
}
