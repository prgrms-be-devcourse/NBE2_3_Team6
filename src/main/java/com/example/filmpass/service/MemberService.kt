package com.example.filmpass.service

import com.example.filmpass.dto.MemberSignupDto
import com.example.filmpass.entity.Member
import com.example.filmpass.jwt.JwtUtil
import com.example.filmpass.repository.MemberRepository
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findById(username).orElseThrow {
            UsernameNotFoundException("User not found")
        }
        return User(member.id ?: "", member.password ?: "", ArrayList<GrantedAuthority>())
    }

    // 회원가입
    fun signup(memberSignupDto: MemberSignupDto) {
        // 중복 체크
        if (memberRepository.findById(memberSignupDto.id ?: throw RuntimeException("Id is required")).isPresent) {
            throw RuntimeException("ID already exists")
        }
        if (memberRepository.findByNumber(memberSignupDto.number ?: throw RuntimeException("Phone number is required")).isPresent) {
            throw RuntimeException("Phone number already exists")
        }

        val member = Member().apply {
            password = passwordEncoder.encode(memberSignupDto.password ?: throw RuntimeException("Password is required"))
            id = memberSignupDto.id ?: throw RuntimeException("ID is required")
            email = memberSignupDto.email
            number = memberSignupDto.number
            image = memberSignupDto.image ?: "default_image.png"
            role = memberSignupDto.role ?: "USER"
        }

        memberRepository.save(member)
    }

    // 멤버 찾기
    fun findById(memberId: String): Member {
        return memberRepository.findById(memberId).orElseThrow { RuntimeException("Member not found") }
    }

    // 로그인
    fun login(id: String, password: String?): Map<String, String> {
        val userDetails = loadUserByUsername(id)

        if (passwordEncoder.matches(password, userDetails.password)) {
            val token = jwtUtil.generateToken(id)
            val refreshToken = jwtUtil.generateRefreshToken(id)

            // 리프레시 토큰을 DB에 저장
            val member = memberRepository.findById(id).orElseThrow {
                UsernameNotFoundException("User not found")
            }
            member.refreshToken = refreshToken // 리프레시 토큰 설정
            memberRepository.save(member) // DB에 업데이트

            return mapOf("accessToken" to token, "refreshToken" to refreshToken)
        } else {
            throw RuntimeException("Invalid credentials")
        }
    }

    // 이미지 업데이트
    fun updateProfileImage(id: String, newImage: String) {
        val member = memberRepository.findById(id).orElseThrow {
            RuntimeException("User not found")
        }
        member.image = newImage
        memberRepository.save(member)
    }
}
