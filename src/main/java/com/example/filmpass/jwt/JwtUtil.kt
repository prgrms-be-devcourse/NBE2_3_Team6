package com.example.filmpass.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.function.Function

@Component
class JwtUtil {
    @Value("\${jwt.secret}")
    private val SECRET_KEY: String? = null

    // JWT에서 사용자 이름 추출
    fun extractUsername(token: String?): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    // JWT 클레임에서 만료 날짜 추출
    fun extractExpiration(token: String?): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    // 특정 클레임 추출
    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    // 모든 클레임 추출
    private fun extractAllClaims(token: String?): Claims {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JWT token")
        }
    }

    // 토큰 만료 여부 확인
    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    // 엑세스 토큰 생성
    fun generateToken(username: String): String {
        val currentTimeMillis = System.currentTimeMillis()

        // 현재 시간을 KST 기준으로 출력
        val nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        println("Current Time (KST - JWT Generation): $nowKST")
        println("JWT 만료 시간 (KST): " + nowKST.plusHours(1)) // 만료 시간을 1시간 후로 출력

        val claims: Map<String, Any?> = HashMap()
        return createToken(claims, username, 1, false) // 1시간 동안 유효한 액세스 토큰
    }


    // 리프레시 토큰 생성 (30일 유효)
    fun generateRefreshToken(username: String): String {
        val claims: Map<String, Any?> = HashMap()
        return createToken(claims, username, 30, true)
    }

    // JWT 생성 메서드
    private fun createToken(
        claims: Map<String, Any?>,
        subject: String,
        duration: Int,
        isRefreshToken: Boolean
    ): String {
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")) // 한국 시간대(KST) 사용
        val issuedAt = Date.from(now.toInstant()) // 발급 시간
        var expiration = Date.from(now.plusHours(duration.toLong()).toInstant()) // 만료 시간 (시간 단위)

        if (isRefreshToken) {
            expiration = Date.from(now.plusDays(duration.toLong()).toInstant()) // 리프레시 토큰은 일 단위
        }

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    // JWT 유효성 검사
    fun validateToken(token: String?, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return (extractedUsername == username && !isTokenExpired(token))
    }
}
