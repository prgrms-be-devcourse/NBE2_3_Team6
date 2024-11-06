package com.example.filmpass.controller

import com.example.filmpass.dto.MemberLoginDto
import com.example.filmpass.dto.MemberProfileDto
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
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/member")
class MemberController(
    private val memberService: MemberService,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
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
    ): ResponseEntity<Map<String, String>> {
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(memberLoginDto.id, memberLoginDto.password)
            )
            val tokens = memberService.login(memberLoginDto.id!!, memberLoginDto.password!!)
            val jwtToken = tokens["accessToken"] ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to "Access token is missing"))
            val refreshToken = tokens["refreshToken"] ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to "Refresh token is missing"))

            // JWT 액세스 토큰을 쿠키에 저장
            val jwtCookie = Cookie("token", jwtToken)
            jwtCookie.isHttpOnly = true
            jwtCookie.maxAge = 60 * 60 // 1시간
            response.addCookie(jwtCookie)

            // JWT 리프레쉬 토큰을 쿠키에 저장
            val refreshCookie = Cookie("refreshToken", refreshToken)
            refreshCookie.isHttpOnly = true
            refreshCookie.maxAge = 60 * 60 * 24 * 30 // 30일
            response.addCookie(refreshCookie)

            // JSON 형식으로 응답
            ResponseEntity.ok(mapOf("message" to "Login successful", "accessToken" to jwtToken, "refreshToken" to refreshToken))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("message" to "로그인 실패: ${e.message}"))
        }
    }
    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<String> {
        // 쿠키에서 토큰 제거
        val jwtCookie = Cookie("token", null)
        jwtCookie.maxAge = 0
        jwtCookie.isHttpOnly = true
        response.addCookie(jwtCookie)

        val refreshCookie = Cookie("refreshToken", null)
        refreshCookie.maxAge = 0
        refreshCookie.isHttpOnly = true
        response.addCookie(refreshCookie)

        return ResponseEntity.ok("Logged out successfully")
    }



    @PutMapping("/profile-image", consumes = ["multipart/form-data"])
    fun updateProfileImage(
        @ModelAttribute memberUpdateDto: MemberUpdateDto,
        request: HttpServletRequest
    ): ResponseEntity<String> {
        // JWT에서 사용자 ID 추출
        val jwt = request.getHeader("Authorization")?.substring(7) // "Bearer " 부분 제거
        val id = jwtUtil.extractId(jwt ?: "") ?: return ResponseEntity.badRequest().body("Invalid token: ID not found")

        val imageFile = memberUpdateDto.image
        if (imageFile == null) {
            return ResponseEntity.badRequest().body("Image file cannot be null")
        }

        // 이미지 파일 처리 및 업데이트
        val imageUrl = memberService.saveProfileImage(imageFile)
        memberService.updateProfileImage(id, imageUrl)

        return ResponseEntity.ok("Profile image updated successfully")
    }

    @PostMapping("/refresh-token")
    fun refreshToken(
        @CookieValue("refreshToken") refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        // 리프레시 토큰이 유효한지 확인
        val id = jwtUtil.extractId(refreshToken) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token") // id null 체크
        val member = memberService.findById(id) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Member not found") // member null 체크

        // DB에 저장된 리프레시 토큰과 비교
        if (member.refreshToken != refreshToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token")
        }

        // 리프레시 토큰이 유효하면 새로운 액세스 토큰 발급
        return if (jwtUtil.validateToken(refreshToken, id)) {
            val newAccessToken = jwtUtil.generateToken(id)

            // 새 액세스 토큰을 쿠키에 저장
            val newJwtCookie = Cookie("token", newAccessToken)
            newJwtCookie.isHttpOnly = true
            newJwtCookie.maxAge = 60 * 60 // 1시간 유효
            response.addCookie(newJwtCookie)

            ResponseEntity.ok("New access token generated")
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token")
        }
    }

    // 추가된 부분: 회원가입 페이지 반환
    @GetMapping("/signup")
    fun getSignupPage(): String {
        return "signup" // src/main/resources/templates/signup.html을 반환
    }

    // 추가된 부분: 로그인 페이지 반환
    @GetMapping("/login")
    fun getLoginPage(): String {
        return "login" // src/main/resources/templates/login.html을 반환
    }

    // 추가된 부분: 프로필 페이지 반환
    @GetMapping("/profile")
    fun getProfilePage(): String {
        return "profile" // src/main/resources/templates/profile.html을 반환
    }

    // 추가된 부분: 프로필 정보 반환
    @GetMapping("/profile-info")
    fun getProfileInfo(request: HttpServletRequest): ResponseEntity<MemberProfileDto> {
        // JWT에서 사용자 ID 추출
        val jwt = request.getHeader("Authorization")?.substring(7) // "Bearer " 부분 제거
        val id = jwtUtil.extractId(jwt ?: "") ?: return ResponseEntity.badRequest().body(null)

        // 사용자 정보 가져오기
        val member = memberService.findById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        // 사용자 정보를 MemberProfileDto로 변환하여 반환
        val memberProfileDto = MemberProfileDto(
            member.id ?: "", // id는 non-nullable로 설정
            member.email ?: "", // email도 non-nullable로 설정할 수 있음
            member.number ?: "", // number도 non-nullable로 설정할 수 있음
            member.profileImageUrl // profileImageUrl은 nullable, 문제가 없어야 함
        )
        return ResponseEntity.ok(memberProfileDto)
    }

}
