package com.example.filmpass.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
class RefundDTO {
    private val reserveId: Long? = null // 주문 번호
    private val amount: Int? = null // 환불할 금액
    private val refundRequestDate: LocalDateTime? = null // 환불 요청 날짜
}
