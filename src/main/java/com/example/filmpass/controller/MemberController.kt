package com.example.filmpass.controller

import com.example.filmpass.dto.MemberLoginDto
import com.example.filmpass.dto.MemberSignupDto
import com.example.filmpass.dto.MemberUpdateDto
import com.example.filmpass.jwt.JwtUtil
import com.example.filmpass.service.MemberService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    @PutMapping("/profile-image")
    fun updateProfileImage(
        @RequestBody memberUpdateDto: MemberUpdateDto,
        request: HttpServletRequest
    ): ResponseEntity<String> {
        val jwt = request.getHeader("Authorization").substring(7)
        val username = jwtUtil.extractUsername(jwt)
        val newImage = memberUpdateDto.image
        memberService.updateProfileImage(username, newImage)
        return ResponseEntity.ok("Profile image updated successfully")
    }

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody memberSignupDto: MemberSignupDto): ResponseEntity<String> {
        memberService.signup(memberSignupDto)
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully")
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody memberLoginDto: MemberLoginDto,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(memberLoginDto.id, memberLoginDto.password)
            )

            val tokens = memberService.login(memberLoginDto.id, memberLoginDto.password)
            val jwtToken = tokens["accessToken"]
            val refreshToken = tokens["refreshToken"]

            val jwtCookie = Cookie("token", jwtToken)
            jwtCookie.isHttpOnly = true
            jwtCookie.maxAge = 60 * 60 * 1
            response.addCookie(jwtCookie)

            val refreshCookie = Cookie("refreshToken", refreshToken)
            refreshCookie.isHttpOnly = true
            refreshCookie.maxAge = 60 * 60 * 24 * 30
            response.addCookie(refreshCookie)

            ResponseEntity.ok("Login successful")
        } catch (e: AuthenticationException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패")
        }
    }
}
