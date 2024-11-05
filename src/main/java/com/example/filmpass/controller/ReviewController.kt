package com.example.filmpass.controller

import com.example.filmpass.dto.ReviewDTO
import com.example.filmpass.dto.ReviewPageRequestDTO
import com.example.filmpass.entity.Review
import com.example.filmpass.service.ReviewService
import lombok.RequiredArgsConstructor
import lombok.extern.log4j.Log4j2
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Log4j2
class ReviewController(private val reviewService: ReviewService) {


    //리뷰 생성
    @PostMapping
    fun register(@RequestBody reviewDTO: ReviewDTO): ResponseEntity<ReviewDTO?> {
        val created = reviewService.register(reviewDTO)
        return ResponseEntity.ok(created)
    }

    //리뷰 검색
    @GetMapping("/{reviewId}")
    fun read(@PathVariable("reviewId") reviewId: Long): ResponseEntity<ReviewDTO> {
        return ResponseEntity.ok(reviewService.read(reviewId))
    }

    @PutMapping("/{reviewId}") //수정
    fun modify(
        @Validated @RequestBody reviewDTO: ReviewDTO,
        @PathVariable("reviewId") reviewId: Long
    ): ResponseEntity<ReviewDTO> {


        val inputReviewer: String = reviewDTO.reviewer.toString()
        val dbReviewer: String = reviewService.read(reviewId).reviewer.toString()

        return ResponseEntity.ok(reviewService.modify(reviewDTO))
    }

    //삭제 - 등록한 사용자와 ADMIN role이 있는 사용자는 삭제 가능하도록 처리
    @DeleteMapping("/{reviewId}")
    fun remove(
        @PathVariable("reviewId") reviewId: Long?
    ): ResponseEntity<String> {
    reviewService.remove(reviewId) //삭제 처리 후
    return ResponseEntity.ok("success")
}
    //목록
    //기본요청 - 1 페이지, 리뷰 5개
    //페이지 번호를 음수로 지정 : 1 이상이어야 합니다
    //사이즈를 5 미만으로 지정 : 5 이상이어야 합니다    //
    @GetMapping("/list/{movieId}")
    fun getList(
        @PathVariable("movieId") movieId: Long?,
        @Validated pageRequestDTO: ReviewPageRequestDTO
    ): ResponseEntity<Page<Review>?> {
        pageRequestDTO.movieId =movieId
        return ResponseEntity.ok(reviewService.getList(pageRequestDTO))
    }
}