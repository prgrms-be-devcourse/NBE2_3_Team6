package com.example.filmpass.service

import com.example.filmpass.dto.ReviewDTO
import com.example.filmpass.dto.ReviewPageRequestDTO
import com.example.filmpass.entity.Movie
import com.example.filmpass.entity.Review
import com.example.filmpass.repository.MovieRepository
import com.example.filmpass.repository.ReviewRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewService (
    private val reviewRepository: ReviewRepository,
    private val movieRepository: MovieRepository
){

    fun register(reviewDTO: ReviewDTO): ReviewDTO {   //등록
        val movieId = reviewDTO.movieId ?: throw IllegalArgumentException("영화가 없습니다")
        val movie: Movie? = movieRepository.findById(movieId)
            .orElseThrow { EntityNotFoundException("해당 영화가 없습니다") }
        val review = reviewDTO.toEntity(movie!!)
        reviewRepository.save(review)
        return ReviewDTO(
            reviewId = review.reviewId,
            content = review.content,
            reviewer = review.reviewer,
            rating = review.rating,
            regDate = review.regDate,
            modDate = review.modDate,
            movieId = review.movie?.movieId)

    }

    fun read(reviewId: Long?): ReviewDTO {     //조회
        val review: Review? = reviewRepository.findById(reviewId ?: throw IllegalArgumentException("리뷰 아이디가 없습니다."))
            .orElseThrow { IllegalArgumentException("리뷰를 찾을 수 없습니다.") }
        return ReviewDTO(
            reviewId = review?.reviewId,
            content = review?.content,
            reviewer = review?.reviewer,
            rating = review?.rating,
            regDate = review?.regDate,
            modDate = review?.modDate,
            movieId = review?.movie?.movieId)
    }

    fun modify(reviewDTO: ReviewDTO): ReviewDTO {    //수정
        val reviewId = reviewDTO.reviewId ?: throw IllegalArgumentException("Review ID is required")
        val review: Review = reviewRepository.findById(reviewId)
            .orElseThrow { IllegalArgumentException("리뷰를 찾을 수 없습니다") }!!
        review.changeContent(reviewDTO.content ?: "")
        review.changeRating(reviewDTO.rating ?: 0)

        reviewRepository.save(review)
        return ReviewDTO(
            reviewId = review.reviewId,
            content = review.content,
            reviewer = review.reviewer,
            rating = review.rating,
            regDate = review.regDate,
            modDate = review.modDate,
            movieId = review.movie?.movieId)

    }

    //삭제
    fun remove(reviewId: Long?) {
        val review: Review? = reviewRepository.findById(reviewId).orElseThrow { IllegalArgumentException("리뷰를 찾을 수 없습니다.")}

        reviewRepository.delete(review)

    }

    //목록
    fun getList(pageRequestDTO: ReviewPageRequestDTO): Page<Review>? {
            val sort = Sort.by("movie.id").ascending()
            val pageable: Pageable = pageRequestDTO.getPageable(sort)
            return reviewRepository.list(pageRequestDTO.movieId, pageable)

    }
}















