package com.example.filmpass.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ReviewPageRequestDTO {
    @Builder.Default
    private val page: @Min(1) Int = 1 //페이지 번호 - 첫번째 페이지 0부터 시작

    @Builder.Default
    private val size: @Min(5) @Max(100) Int = 5 //한 페이지 게시물 수

    var movieId: Long? = null

    fun getPageable(sort: Sort?): Pageable {  //페이지번호, 페이지 게시물 수, 정렬 순서를 Pageable 객체로 반환
        return PageRequest.of(page - 1, size, sort!!)
    }
}
