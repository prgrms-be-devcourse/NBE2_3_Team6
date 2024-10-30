package com.example.filmpass.dto

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

class RefundDTO {
    val reserveId: Long? = null // 주문 번호
    val amount: Int? = null // 환불할 금액
    var refundRequestDate: LocalDateTime? = null // 환불 요청 날짜
}
