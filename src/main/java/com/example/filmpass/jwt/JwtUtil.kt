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
    private lateinit var secretKey: String // 변수를 소문자로 시작하는 컨벤션에 맞게 변경

    // JWT에서 사용자 ID 추출
    fun extractId(token: String?): String? {
        return extractClaim(token) { claims: Claims -> claims.subject }
    }

    // JWT 클레임에서 만료 날짜 추출
    fun extractExpiration(token: String?): Date? {
        return extractClaim(token) { claims: Claims -> claims.expiration }
    }

    // 특정 클레임 추출
    private fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    // 모든 클레임 추출
    private fun extractAllClaims(token: String?): Claims {
        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid JWT token: ${e.message}", e) // 오류 메시지 추가
        }
    }

    // 토큰 만료 여부 확인
    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token)?.before(Date()) ?: true // null 처리 추가
    }

    // 엑세스 토큰 생성
    fun generateToken(id: String): String {
        val nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        println("Current Time (KST - JWT Generation): $nowKST")
        println("JWT 만료 시간 (KST): ${nowKST.plusHours(1)}")

        val claims = emptyMap<String, Any?>() // 빈 맵으로 초기화
        return createToken(claims, id, 1, false)
    }

    // 리프레시 토큰 생성 (30일 유효)
    fun generateRefreshToken(id: String): String {
        val claims = emptyMap<String, Any?>() // 빈 맵으로 초기화
        return createToken(claims, id, 30, true)
    }

    // JWT 생성 메서드
    private fun createToken(
        claims: Map<String, Any?>,
        subject: String,
        duration: Int,
        isRefreshToken: Boolean
    ): String {
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val issuedAt = Date.from(now.toInstant())
        val expiration = if (isRefreshToken) {
            Date.from(now.plusDays(duration.toLong()).toInstant())
        } else {
            Date.from(now.plusHours(duration.toLong()).toInstant())
        }

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    // JWT 유효성 검사
    fun validateToken(token: String?, id: String): Boolean {
        val extractedId = extractId(token)
        return (extractedId == id && !isTokenExpired(token))
    }
}
