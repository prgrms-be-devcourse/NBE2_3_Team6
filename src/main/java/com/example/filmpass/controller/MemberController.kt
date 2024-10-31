package com.example.filmpass.controller

import com.example.filmpass.dto.MemberLoginDto
import com.example.filmpass.dto.MemberSignupDto
import com.example.filmpass.service.MemberService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService,
    private val authenticationManager: AuthenticationManager
) {

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
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(memberLoginDto.id, memberLoginDto.password)
        )
        val tokens = memberService.login(memberLoginDto.id!!, memberLoginDto.password!!)
        val jwtToken = tokens["accessToken"]
        val refreshToken = tokens["refreshToken"]

        val jwtCookie = Cookie("token", jwtToken)
        jwtCookie.isHttpOnly = true
        response.addCookie(jwtCookie)

        val refreshCookie = Cookie("refreshToken", refreshToken)
        refreshCookie.isHttpOnly = true
        response.addCookie(refreshCookie)

        return ResponseEntity.ok("Login successful")
    }
}
